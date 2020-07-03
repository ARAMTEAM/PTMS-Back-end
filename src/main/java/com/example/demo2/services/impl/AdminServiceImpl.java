package com.example.demo2.services.impl;

import com.example.demo2.dao.AdminDao;
import com.example.demo2.dao.RefreshTokenDao;
import com.example.demo2.dao.SettingDao;
import com.example.demo2.dao.SlogsDao;
import com.example.demo2.pojo.Admin;
import com.example.demo2.pojo.RefreshToken;
import com.example.demo2.pojo.Setting;
import com.example.demo2.pojo.Slogs;
import com.example.demo2.response.ResponseResult;
import com.example.demo2.services.IAdminService;
import com.example.demo2.services.SlogsService;
import com.example.demo2.utils.*;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class AdminServiceImpl implements IAdminService {

    @Autowired
    private AdminDao adminDao;
    @Autowired
    private SettingDao settingDao;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private RefreshTokenDao refreshTokenDao;
    @Autowired
    private SlogsService slogsService;
    @Autowired
    private SlogsDao slogsDao;


    @Override
    public ResponseResult initManagerAccount(Admin admin) {

        //检查是否已经初始化
        Setting managerAccountState = settingDao.findOneByAkey(Constants.Setting.MANAGER_ACCOUNT_INIT_STATE);
        if(managerAccountState != null){
            return ResponseResult.FAILED("管理员账户已经初始化过了！");
        }

        //检查数据
        if (TextUtils.isEmpty(admin.getAdminId())){
            return ResponseResult.FAILED("用户名不能为空！");
        }
        if (TextUtils.isEmpty(admin.getAdminPassword())){
            return ResponseResult.FAILED("密码不能为空！");
        }

        //对密码进行加密
        String password = admin.getAdminPassword();
        String encode = bCryptPasswordEncoder.encode(password);
        admin.setAdminPassword(encode);


        //保存至数据库中
        adminDao.save(admin);

        //更新标记
        Setting setting = new Setting();
        setting.setId("id01");
        setting.setCreateTime(new Date());
        setting.setUpdateTime(new Date());
        setting.setAkey(Constants.Setting.MANAGER_ACCOUNT_INIT_STATE);
        setting.setAvalue("1");  //1表示存在，0表示删除
        settingDao.save(setting);


        return ResponseResult.SUCCESS("初始化管理员账户成功!");
    }


    /**
     * 管理员登录
     *
     * @param admin
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult doLogin(Admin admin, HttpServletRequest request, HttpServletResponse response) {
        String admin_id = admin.getAdminId();
        log.info("管理员  " +admin_id+"  正在登录");
        if(TextUtils.isEmpty(admin_id)){
            return ResponseResult.FAILED("账号不可以为空!");
        }
        String password = admin.getAdminPassword();
        if(TextUtils.isEmpty(password)){
            return ResponseResult.FAILED("密码不可以为空!");
        }
        Admin adminByAdminId = adminDao.findOneByAdminId(admin_id);
        //判断用户是否用户存在
        if(adminByAdminId == null){
            log.info("管理员  " +admin_id+"  不存在");
            return ResponseResult.FAILED("账号或密码错误!");
        }
        //判断密码是否正确
        if(!bCryptPasswordEncoder.matches(password,adminByAdminId.getAdminPassword())){
            log.info("管理员  " +admin_id+"  账号或密码错误！");
            return ResponseResult.FAILED("账号或密码错误");
        }
        log.info("验证成功，创建token");
        createAdminToken(response, adminByAdminId);
        log.info("管理员  " +admin_id+"  登录成功！");
        slogsService.savelog(Constants.User.UserType_admin,adminByAdminId.getAdminId(),Constants.operation.OP_LOGIN,IPUtil.getIpAddr(request));
        return ResponseResult.SUCCESS("登录成功！").setData(admin_id);
    }

    /**
     * 返回tokenkey
     *
     * @param response
     * @param adminByAdminId
     * @return tokenkey
     */
    private String createAdminToken(HttpServletResponse response, Admin adminByAdminId) {
        //删除旧的refreshtoken
        int deleteResult = refreshTokenDao.deleteAllByUserIdAndUserType(adminByAdminId.getAdminId(),Constants.User.UserType_admin);
        log.info("the num of deleteed results is == > "+deleteResult);
        //生成token
        //存放返回的内容，注意不能返回密码
        Map<String,Object> claims = ClaimsUtils.adminToclaims(adminByAdminId);
        //token默认有效2小时
        String token = JwtUtil.createToken(claims);
        log.info(token);
        //返回token的md5值，保存在redis中
        //前端访问时，携带token的md5key，从redis中获取即可
        String tokenKey = DigestUtils.md5DigestAsHex(token.getBytes());
        //保存token到redis中，有效期两个小时，key是tokenKey
        redisUtils.set(Constants.User.KEY_TOKEN + tokenKey, token, Constants.TimeValue.HOUR_2);
        //把tokenKey写到cookies里
        //这个要动态获取，可以从request中获取
        CookieUtils.setUpCookie(response, Constants.User.COOKIE_TOKE_KEY, tokenKey);
        //生成refreshtoken,有效期一天
        String refreshtokenvalue = JwtUtil.createRefreshToken(adminByAdminId.getAdminId(),Constants.TimeValue.DAY);
        //保存到数据库
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(refreshtokenvalue);
        refreshToken.setUserId(adminByAdminId.getAdminId());
        refreshToken.setUserType(Constants.User.UserType_admin);
        refreshToken.setTokenKey(tokenKey);
        refreshToken.setCreateTime(new Date());
        refreshToken.setUpdateTime(new Date());
        refreshTokenDao.save(refreshToken);
        //返回token的md5值
        return tokenKey;
    }

    /**
     * 检查管理员是否有登录，如果登录了就返回用户信息
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    public Admin checkAdmin(HttpServletRequest request, HttpServletResponse response) {
        //拿到tokenKey
            String tokenKey = CookieUtils.getCookie(request,Constants.User.COOKIE_TOKE_KEY);
        //解析tokenkey，并取出其中管理员ID
            Admin admin = parseByTokenKey(tokenKey);
            if(admin == null){
                //解析出错，token已经过期
                //去mysql中查询refreshtoken
//                log.info("token已经过期,去查找refreshtoken...");
                RefreshToken refreshToken = refreshTokenDao.findOneByTokenKey(tokenKey);
                // 如果不存在就返回没登录
                if(refreshToken == null){
                    log.info("未登录");
                    return null;
                }
                try{
                    JwtUtil.parseJWT(refreshToken.getRefreshToken());
                    // 如果存在就解析refreshtoken，并创建新的token和新的token
                    String userId = refreshToken.getUserId();
                    Admin adminByAdminId = adminDao.findOneByAdminId(userId);
                    //创建新的token
                    String newTokenKey = createAdminToken(response,adminByAdminId);
                    //返回token
//                    log.info("创建新的管理员token和refreshtoken...");
                    return parseByTokenKey(newTokenKey);
                }catch (Exception e1){
//                    log.info("refreshToken 过期...需要登录");
                    //如果refreshtoken过期，当前访问未登录，提示用户登录
                    log.info("未登录");
                    return null;
                }
            }
            //返回的admin不为空，那么可以直接返回这个admin
//            log.info("已登录");
            return admin;
    }


    /**
     * 退出登录
     *
     * @return
     */
    @Override
    public ResponseResult doLogout(HttpServletRequest request,HttpServletResponse response) {

        //拿到tokenkey
        String tokenKey = CookieUtils.getCookie(getRequest(),Constants.User.COOKIE_TOKE_KEY);
        if(TextUtils.isEmpty(tokenKey)){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("管理员开始退出登录");
        Admin admin = parseByTokenKey(tokenKey);
        if(admin == null || admin.getAdminId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //删除redis中的token
        redisUtils.del(Constants.User.KEY_TOKEN+tokenKey);
        //删除mysql中的refreshtoken
        int result = refreshTokenDao.deleteAllByTokenKey(tokenKey);
        //删除cookie
        CookieUtils.deleteCookie(getResponse(),getRequest(),Constants.User.COOKIE_TOKE_KEY);
        slogsService.savelog(Constants.User.UserType_admin,admin.getAdminId(),Constants.operation.OP_LOGOUT,IPUtil.getIpAddr(request));
        log.info("退出登录成功");
        log.info("");
        log.info("");
        log.info("");
        return ResponseResult.SUCCESS("退出登录成功！");
    }



    private Admin parseByTokenKey(String tokenKey){
        //通过cookie传来的tokenkey，和KEY_TOKEN组合后，从redis中取出真正的token
        String token = (String) redisUtils.get(Constants.User.KEY_TOKEN + tokenKey);
        if(token != null){
            try{
                //解析token，生成claims
                Claims claims = JwtUtil.parseJWT(token);
                //返回claim对应的admin
                return ClaimsUtils.claimsToadmin(claims);
            }catch (Exception e){
                log.info("无法解析为管理员");
                return null;
            }
        }
        log.info("token为空");
        return null;
    }

    private HttpServletRequest getRequest(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }

    private HttpServletResponse getResponse(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getResponse();
    }
}
