package com.example.demo2.services.impl;

import com.example.demo2.dao.JiaowuDao;
import com.example.demo2.dao.RefreshTokenDao;
import com.example.demo2.pojo.Admin;
import com.example.demo2.pojo.Jiaowu;
import com.example.demo2.pojo.RefreshToken;
import com.example.demo2.response.ResponseResult;
import com.example.demo2.services.IAdminService;
import com.example.demo2.services.JiaowuService;
import com.example.demo2.services.SlogsService;
import com.example.demo2.utils.*;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.example.demo2.utils.UpdateUtil.getNullPropertyNames;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class JiaowuServiceImpl implements JiaowuService {

    @Autowired
    private JiaowuDao jiaowuDao;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private IAdminService adminService;
    @Autowired
    private RefreshTokenDao refreshTokenDao;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private SlogsService slogsService;


    /**
     * 教务登录
     *
     * @param jiaowu
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult JiaowuLogin(Jiaowu jiaowu, HttpServletRequest request, HttpServletResponse response) {
        String jiaowuId = jiaowu.getJiaowuId();
        log.info("登录的教务ID为:  "+jiaowuId);
        String jiaowuPassword = jiaowu.getJiaowuPassword();
        log.info("登录的教务密码为:  "+jiaowuPassword);
        if(TextUtils.isEmpty(jiaowuId)||TextUtils.isEmpty(jiaowuPassword)){
            return ResponseResult.FAILED("账号或密码不可以为空!");
        }
        Jiaowu jiaowuById = jiaowuDao.findOneByJiaowuId(jiaowuId);
        if(jiaowuById == null){
            log.info("教务不存在");
            return ResponseResult.FAILED("账号或密码错误！");
        }
        if(!bCryptPasswordEncoder.matches(jiaowuPassword,jiaowuById.getJiaowuPassword())){
            log.info("密码错误");
            return ResponseResult.FAILED("账号或密码错误！");
        }
        createJiaowuToken(response,jiaowuById);
        log.info("教务"+jiaowuId+"登录成功!");
        return ResponseResult.SUCCESS("教务登录成功!").setData(jiaowuId);
    }

    /**
     * 创建教务token
     *
     * @param response
     * @param jiaowuById
     * @return
     */

    private String createJiaowuToken(HttpServletResponse response,Jiaowu jiaowuById){
        log.info("教务登录成功，开始创建token……");
        int deleteResult = refreshTokenDao.deleteAllByUserIdAndUserType(jiaowuById.getJiaowuId(),Constants.User.UserType_jiaowu);
        log.info("the num of deleteed results is == > "+deleteResult);
        Map<String,Object> claims = ClaimsUtils.jiaowuToclaims(jiaowuById);
        String token = JwtUtil.createToken(claims);
        String tokenKey = DigestUtils.md5DigestAsHex(token.getBytes());
        redisUtils.set(Constants.User.KEY_TOKEN + tokenKey,token,Constants.TimeValue.HOUR_2);
        CookieUtils.setUpCookie(response,Constants.User.COOKIE_TOKE_KEY,tokenKey);
        String refreshtokenvalue = JwtUtil.createRefreshToken(jiaowuById.getJiaowuId(),Constants.TimeValue.DAY);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(refreshtokenvalue);
        refreshToken.setUserId(jiaowuById.getJiaowuId());
        refreshToken.setUserType(Constants.User.UserType_jiaowu);
        refreshToken.setTokenKey(tokenKey);
        refreshToken.setCreateTime(new Date());
        refreshToken.setUpdateTime(new Date());
        refreshTokenDao.save(refreshToken);
        //返回token的md5值
        return tokenKey;
    }


    //检查教务是否登录
    @Override
    public Jiaowu checkJiaowu(HttpServletRequest request, HttpServletResponse response) {
        String tokenKey = CookieUtils.getCookie(request,Constants.User.COOKIE_TOKE_KEY);
//        log.info("试图把token解析为教务");
        Jiaowu jiaowu = parseByTokenKey(tokenKey);
        if(jiaowu == null){
            RefreshToken refreshToken = refreshTokenDao.findOneByTokenKey(tokenKey);
            if(refreshToken == null){
                return null;
            }
            try{
                JwtUtil.parseJWT(refreshToken.getRefreshToken());
                String userId = refreshToken.getUserId();
                Jiaowu jiaowuById = jiaowuDao.findOneByJiaowuId(userId);
                log.info("创建新的token和refreshtoken...");
                String newTokenKey = createJiaowuToken(response,jiaowuById);
                return parseByTokenKey(newTokenKey);
            }catch (Exception e){
                log.info("refreshToken 过期...");
                //如果refreshtoken过期，当前访问未登录，提示用户登录
                return null;
            }
        }
        return jiaowu;
    }

    /**
     * 退出登录
     *
     * @return
     */
    @Override
    public ResponseResult jiaowuLogout() {
        log.info("教务开始执行退出登录");
        //拿到tokenkey
        String tokenKey = CookieUtils.getCookie(getRequest(),Constants.User.COOKIE_TOKE_KEY);
        if(TextUtils.isEmpty(tokenKey)){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //删除redis中的token
        redisUtils.del(Constants.User.KEY_TOKEN+tokenKey);
        //删除mysql中的refreshtoken
        int result = refreshTokenDao.deleteAllByTokenKey(tokenKey);
        //删除cookie
        CookieUtils.deleteCookie(getResponse(),getRequest(),Constants.User.COOKIE_TOKE_KEY);
        CookieUtils.deleteCookie(getResponse(),getRequest(),"jiaowuId");
        log.info("退出登录成功");
        log.info("");
        log.info("");
        log.info("");
        return ResponseResult.SUCCESS("退出登录成功！");
    }

    @Override
    public ResponseResult listOneJiaowu(String jiaowuId) {
        //检查教务是否存在
        Jiaowu jiaowu = jiaowuDao.listOneJiaowu(jiaowuId);
        if (jiaowu != null) {
            return ResponseResult.SUCCESS("查询成功").setData(jiaowu);
        }else {
            return ResponseResult.FAILED("教务不存在！");
        }
    }


    /**
     * 增加一个教务
     *
     * @param jiaowu
     * @return
     */
    @Override
    public ResponseResult createNewJiaowu(Jiaowu jiaowu,HttpServletRequest request,HttpServletResponse response) {
        Admin admin = adminService.checkAdmin(request,response);
        log.info("创建教务...开始检查字段");
        //检查数据
        if (TextUtils.isEmpty(jiaowu.getJiaowuName())){
            return ResponseResult.FAILED("教务名称不能为空！");
        }
        if (TextUtils.isEmpty(jiaowu.getJiaowuUsername())){
            return ResponseResult.FAILED("教务用户名不能为空！");
        }
        if (TextUtils.isEmpty(jiaowu.getJiaowuPassword())){
            return ResponseResult.FAILED("教务密码不能为空！");
        }
        if (TextUtils.isEmpty(jiaowu.getJiaowuNianji())){
            return ResponseResult.FAILED("教务年级不能为空！");
        }
        //检查用户名是否已经存在
        String jiaowuId = jiaowu.getJiaowuId();
        Jiaowu jiaowuById = jiaowuDao.findOneByJiaowuId(jiaowuId);
        if(jiaowuById != null){
            return ResponseResult.FAILED("教务ID已经存在！请更换ID后再试！");
        }
        log.info("检查通过");
        //对密码进行加密
        String password = jiaowu.getJiaowuPassword();
        String encode = bCryptPasswordEncoder.encode(password);
        log.info("正在加密密码");
        jiaowu.setJiaowuPassword(encode);
        jiaowuDao.save(jiaowu);
        slogsService.savelog(Constants.User.UserType_admin,admin.getAdminId(),Constants.operation.OP_ADDJIAOWU+" "+jiaowu.getJiaowuId(),IPUtil.getIpAddr(request));
        return ResponseResult.SUCCESS("创建教务成功！");
    }

    /**
     * 删除一个教务
     *
     * @param jiaowuId
     * @return
     */
    @Override
    public ResponseResult deleteOneJiaowu(String jiaowuId,HttpServletRequest request,HttpServletResponse response) {
        Admin admin = adminService.checkAdmin(request,response);
        log.info("准备删除教务  "+jiaowuId);
        //检查输入的ID是否存在
        Jiaowu jiaowuFromDbByID = jiaowuDao.findOneByJiaowuId(jiaowuId);
        if(jiaowuFromDbByID == null){
            return ResponseResult.FAILED("所要删除的教务不存在！请检查教务ID");
        }
        //执行删除操作
        log.info("正在删除教务  "+jiaowuId);
        jiaowuDao.deleteById(jiaowuId);


        //再次检查是否删除
        Jiaowu jiaowuFromDbByID2 = jiaowuDao.findOneByJiaowuId(jiaowuId);
        if(jiaowuFromDbByID2 == null){
            log.info("删除成功");
            log.info("");
            log.info("");
            log.info("");
            slogsService.savelog(Constants.User.UserType_admin,admin.getAdminId(),Constants.operation.OP_DELETEJIAOWU+" "+jiaowuId,IPUtil.getIpAddr(request));
            return ResponseResult.SUCCESS("删除成功！");
        }
        log.info("删除失败");
        log.info("");
        log.info("");
        log.info("");
        return ResponseResult.FAILED("删除失败！");
    }

    /**
     * 更新教务
     *
     * @param jiaowu
     * @return
     */
    @Override
    public ResponseResult updateOneJiaowu(Jiaowu jiaowu,HttpServletRequest request,HttpServletResponse response) {
        Admin admin = adminService.checkAdmin(request,response);
        //先把原教务查询出来
        if(jiaowu.getJiaowuId() == null){
            log.info("字段为空");
            return ResponseResult.FAILED("ID字段为空");
        }
        log.info("准备更新教务  "+jiaowu.getJiaowuId());
        Jiaowu jiaowuFromDbByID = jiaowuDao.findOneByJiaowuId(jiaowu.getJiaowuId());
        if(jiaowuFromDbByID == null){
            return ResponseResult.FAILED("所要更新的教务不存在！请检查后再试！");
        }
//        System.out.println("密码是：  "+ jiaowu.getJiaowuPassword());

        //检查更换的字段
        BeanUtils.copyProperties(jiaowu, jiaowuFromDbByID, getNullPropertyNames(jiaowu));

        //如果传回来的密码为空，就不再修改
        //如果不为空，就再加密一次存入数据库中
        if(jiaowu.getJiaowuPassword() != null){
            String password = jiaowu.getJiaowuPassword();
            if(TextUtils.isLegal(password)){
                String encode = bCryptPasswordEncoder.encode(password);
                jiaowuFromDbByID.setJiaowuPassword(encode);
                log.info("更新教务密码  "+encode);
            }

        }
        //存入数据库
        jiaowuDao.save(jiaowuFromDbByID);
        slogsService.savelog(Constants.User.UserType_admin,admin.getAdminId(),Constants.operation.OP_UPDATEJIAOWU+" "+jiaowu.getJiaowuId(),IPUtil.getIpAddr(request));
        log.info("");
        log.info("");
        log.info("");
        return ResponseResult.SUCCESS("更新成功");
    }

    @Override
    public ResponseResult findAlljiaowu() {
        List<Jiaowu> all = jiaowuDao.findAll();
        return ResponseResult.SUCCESS("查询成功").setData(all);
    }

    /**
     * 分页列出教务
     * 需要管理员权限
     *
     * @param page
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult listJiaowu(int page, HttpServletRequest request, HttpServletResponse response) {
        //检查是否登录
        Admin admin = adminService.checkAdmin(request,response);
        if(admin == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(admin.getAdminId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //可以获取列表
        //分页查询
        if(page < 1){
            page = 1;
        }
        log.info("申请查询分页教务");
        //根据教务ID排序
        Sort sort = Sort.by(Sort.Direction.ASC,"jiaowuId");
        Pageable pageable = PageRequest.of(page -1, Constants.Page.DEFAUT_PAGE_SIZE,sort);
        Page<Jiaowu> all = jiaowuDao.listAllJiaowuNoPassword(pageable);
        return ResponseResult.SUCCESS("查询成功").setData(all);
    }

    private Jiaowu parseByTokenKey(String tokenKey){
        //通过cookie传来的tokenkey，和KEY_TOKEN组合后，从redis中取出真正的token
        String token = (String) redisUtils.get(Constants.User.KEY_TOKEN + tokenKey);
        if(token != null){
            try{
                //解析token，生成claims
                Claims claims = JwtUtil.parseJWT(token);
                //返回claim对应的admin
                return ClaimsUtils.claimsTojiaowu(claims);
            }catch (Exception e){
                log.info("无法解析为教务");
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
