package com.example.demo2.services.impl;

import com.example.demo2.dao.RefreshTokenDao;
import com.example.demo2.dao.StudentDao;
import com.example.demo2.dao.TeacherDao;
import com.example.demo2.dao.TrainingDao;
import com.example.demo2.pojo.*;
import com.example.demo2.response.ResponseResult;
import com.example.demo2.services.JiaowuService;
import com.example.demo2.services.StudentService;
import com.example.demo2.services.TeacherService;
import com.example.demo2.utils.*;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.example.demo2.utils.UpdateUtil.getNullPropertyNames;

@Service
@Slf4j
@Transactional
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherDao teacherDao;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private RefreshTokenDao refreshTokenDao;
    @Autowired
    private JiaowuService jiaowuService;
    @Autowired
    private TrainingDao trainingDao;
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentDao studentDao;


    /**
     * 教师登录
     *
     * @param teacher
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult TeahcerLogin(Teacher teacher, HttpServletRequest request, HttpServletResponse response) {
        String teacherId = teacher.getTeacherId();
        String password = teacher.getTeacherPassword();
        log.info("教师  "+teacherId+"  正在登录");
        if(TextUtils.isEmpty(teacherId) || TextUtils.isEmpty(password)){
            return ResponseResult.FAILED("账号或密码不可以为空!");
        }
        if(TextUtils.isEmpty(password)){
            return ResponseResult.FAILED("密码不可以为空!");
        }
        Teacher teacherById = teacherDao.findTeacherByTeacherId(teacherId);
        //判断用户是否用户存在
        if(teacherById == null){
            log.info("教师  "+teacherId+"  不存在");
            return ResponseResult.FAILED("账号或密码错误!");
        }
        //判断密码是否正确
        if(teacherById.getTeacherPassword().equals("000000")){
            if(password.equals("000000")){
                createTeacherToken(response, teacherById);
                log.info("教师  "+teacherId+"  以初始密码登录成功");
                return ResponseResult.SUCCESS("登录成功！");
            }
        }
        if(!bCryptPasswordEncoder.matches(password,teacherById.getTeacherPassword())){
            return ResponseResult.FAILED("账号或密码错误");
        }
        return ResponseResult.SUCCESS("登录成功！").setData(teacherId);
    }

    /**
     * 查出登录的教师
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    public Teacher checkTeacher(HttpServletRequest request, HttpServletResponse response) {
        //拿到tokenKey
        String tokenKey = CookieUtils.getCookie(request,Constants.User.COOKIE_TOKE_KEY);
        //解析tokenkey，并取出其中管理员ID
        Teacher teacher = parseByTokenKey(tokenKey);
        if(teacher == null){
            //解析出错，token已经过期
            //去mysql中查询refreshtoken
            RefreshToken refreshToken = refreshTokenDao.findOneByTokenKey(tokenKey);
            // 如果不存在就返回没登录
            if(refreshToken == null){
                return null;
            }
            try{
                JwtUtil.parseJWT(refreshToken.getRefreshToken());
                // 如果存在就解析refreshtoken，并创建新的token和新的token
                String userId = refreshToken.getUserId();
                Teacher teacherById = teacherDao.findTeacherByTeacherId(userId);
                //创建新的token
                String newTokenKey = createTeacherToken(response,teacherById);
                //返回token
                log.info("创建新的token和refreshtoken...");
                return parseByTokenKey(newTokenKey);
            }catch (Exception e1){
                log.info("refreshToken 过期...");
                //如果refreshtoken过期，当前访问未登录，提示用户登录
                return null;
            }
        }
        //返回的admin不为空，那么可以直接返回这个admin
        return teacher;
    }



    /**
     * 退出登录
     *
     * @return
     */
    @Override
    public ResponseResult doLogout() {
        log.info("教师开始退出登录");
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
        log.info("退出登录成功");
        log.info("");
        log.info("");
        log.info("");
        return ResponseResult.SUCCESS("退出登录成功！");
    }



    /**
     * 创建教师token
     *
     * @param response
     * @param teacherById
     * @return
     */
    private String createTeacherToken(HttpServletResponse response, Teacher teacherById) {
        //删除旧的refreshtoken
        int deleteResult = refreshTokenDao.deleteAllByUserIdAndUserType(teacherById.getTeacherId(),Constants.User.UserType_teacher);
        log.info("the num of deleteed results is == > "+deleteResult);
        //生成token
        //存放返回的内容，注意不能返回密码
        Map<String,Object> claims = ClaimsUtils.teacherToclaims(teacherById);
        //token默认有效2小时
        String token = JwtUtil.createToken(claims);
        //返回token的md5值，保存在redis中
        //前端访问时，携带token的md5key，从redis中获取即可
        String tokenKey = DigestUtils.md5DigestAsHex(token.getBytes());
        //保存token到redis中，有效期两个小时，key是tokenKey
        redisUtils.set(Constants.User.KEY_TOKEN + tokenKey, token, Constants.TimeValue.HOUR_2);
        //把tokenKey写到cookies里
        //这个要动态获取，可以从request中获取
        CookieUtils.setUpCookie(response, Constants.User.COOKIE_TOKE_KEY, tokenKey);
        //生成refreshtoken,有效期一天
        String refreshtokenvalue = JwtUtil.createRefreshToken(teacherById.getTeacherId(),Constants.TimeValue.DAY);
        //保存到数据库
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(refreshtokenvalue);
        refreshToken.setUserId(teacherById.getTeacherId());
        refreshToken.setUserType(Constants.User.UserType_teacher);
        refreshToken.setTokenKey(tokenKey);
        refreshToken.setCreateTime(new Date());
        refreshToken.setUpdateTime(new Date());
        refreshTokenDao.save(refreshToken);
        //返回token的md5值
        return tokenKey;
    }




    /**
     * 查询教务名下的所有教师
     *
     * @param request
     * @param response
     * @param page
     * @return
     */
    @Override
    public ResponseResult listAllTeacherWithoutPassword(HttpServletRequest request,
                                                        HttpServletResponse response,
                                                        Integer page) {
        //验证教务登录并获取教务ID
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(jiaowu == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(jiaowu.getJiaowuId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("教务  "+jiaowu.getJiaowuId()+"  正在查询他管辖的所有教师");
        String jiaowuId = jiaowu.getJiaowuId();
        log.info("校验通过");
        Pageable pageable = PageRequest.of(page -1, Constants.Page.DEFAUT_PAGE_SIZE);
        Page<List<Map<String,Object>>> all = teacherDao.findAllteacherWithoutPasswordByjiaowuid(jiaowuId,pageable);
        log.info("查询成功");
        log.info("");
        return ResponseResult.SUCCESS("查询所有教师成功！").setData(all);
    }

    /**
     * 不分页列出所有教师
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult listAllTeacherByJiaowuNoPage(HttpServletRequest request, HttpServletResponse response ) {
        //验证教务登录并获取教务ID
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(jiaowu == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(jiaowu.getJiaowuId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("教务  "+jiaowu.getJiaowuId()+"  正在查询他管辖的所有教师");
        String jiaowuId = jiaowu.getJiaowuId();
        log.info("校验通过");
        List<Map<String,Object>> all = teacherDao.listAllTeacherByJiaowuNoPage(jiaowuId);
        log.info("查询成功");
        log.info("");
        return ResponseResult.SUCCESS("查询所有教师成功！").setData(all);

    }

    /**
     * 学生列出所有可以选择的教师
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult listAllTeacherToChoose(HttpServletRequest request, HttpServletResponse response) {
        Student student = studentService.checkStudent(request,response);
        if(student == null || student.getStudentId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        Student studentById = studentDao.findStudentByStudentId(student.getStudentId());
        log.info("学生  "+studentById.getStudentId()+"  正在查询所有可以被指定的指导教师");
        int trainingId = studentById.getTrainingId();
        List<Map<String,Object>> result = teacherDao.listAllTeacherCanChoose(trainingId);
        log.info("查询成功");
        return ResponseResult.SUCCESS("查询成功").setData(result);
    }

    @Override
    public ResponseResult getSumNum() {
        return ResponseResult.SUCCESS("查询成功").setData(teacherDao.getTeacherNum());
    }


    /**
     * 增加教师
     *
     * @param teacher
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult addTeacher(Teacher teacher, HttpServletRequest request, HttpServletResponse response) {
        //检查教务是否登录，需要教务权限，并获取jiaowuId
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(jiaowu == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(jiaowu.getJiaowuId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("教务  "+jiaowu.getJiaowuId()+"  正在增加教师  "+teacher.getTeacherId()+"  的信息");
        //校验当前的教务ID是否可以操作这个实训
        String jiaowuId = jiaowu.getJiaowuId();
        Training training = trainingDao.findByTrainingId(teacher.getTrainingId());
        if(!training.getJiaowuId().equals(jiaowuId)){
            return ResponseResult.FAILED("不能把教师添加到不属于自己的实训!");
        }
        //校验完成
        log.info("校验通过");
        teacher.setTeacherPassword("000000");
        log.info("正在添加");
        teacherDao.save(teacher);
        log.info("添加成功");
        log.info("");
        return ResponseResult.SUCCESS("添加成功");
    }

    /**
     * 更新教师
     *
     * @param teacher
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult updateStudent(Teacher teacher, HttpServletRequest request, HttpServletResponse response) {

        Teacher teacherById = teacherDao.findTeacherByTeacherId(teacher.getTeacherId());

        //检查教务是否登录，需要教务权限，并获取jiaowuId
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(jiaowu == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(jiaowu.getJiaowuId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("教务  "+jiaowu.getJiaowuId()+"  正在更新教师  "+teacher.getTeacherId()+"  的信息");
        //校验当前的教务ID是否可以操作这个实训
        String jiaowuId = jiaowu.getJiaowuId();
        Training training = trainingDao.findByTrainingId(teacher.getTrainingId());
        if(!training.getJiaowuId().equals(jiaowuId)){
            return ResponseResult.FAILED("不能修改不属于自己实训的教师!");
        }
        //不能修改教师的实训,请删除后再添加
        if(teacher.getTrainingId() != teacherById.getTrainingId() && TextUtils.isEmpty(String.valueOf(teacher.getTrainingId()))){
            return ResponseResult.FAILED("不能修改教师所在的实训!请删除教师再重新添加");
        }
        //判断是否需要更新密码
        if(teacher.getTeacherPassword() != null){
            String password = teacher.getTeacherPassword();
            if(TextUtils.isLegal(password)){
                String encode = bCryptPasswordEncoder.encode(password);
                teacher.setTeacherPassword(encode);
                log.info("更新教师密码  "+encode);
            }else{
                return ResponseResult.FAILED("密码长度必须不少于6位字符");
            }

        }
        log.info("校验通过");
        //检查需要更新的字段
        BeanUtils.copyProperties(teacher, teacherById, getNullPropertyNames(teacher));
        //存入数据库
        log.info("正在更新");
        teacherDao.save(teacherById);
        log.info("更新成功");
        log.info("");
        return ResponseResult.SUCCESS("更新教师信息成功！");
    }

    /**
     * 删除教师
     *
     * @param teacherId
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult deleteStudent(String teacherId, HttpServletRequest request, HttpServletResponse response) {
        //检查教务是否登录，需要教务权限，并获取jiaowuId
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(jiaowu == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(jiaowu.getJiaowuId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("教务  "+jiaowu.getJiaowuId()+"  正在删除教师  "+teacherId+"  的信息");
        //检查学生是否存在
        if(teacherDao.findTeacherByTeacherId(teacherId) == null){
            return ResponseResult.FAILED("删除的教师不存在!");
        }
        Teacher teacherById = teacherDao.findTeacherByTeacherId(teacherId);
        //校验当前的教务ID是否可以操作这个实训
        String jiaowuId = jiaowu.getJiaowuId();
        Training trainingById = trainingDao.findByTrainingId(teacherById.getTrainingId());
        if(trainingById.getJiaowuId() != jiaowuId){
            return ResponseResult.FAILED("不能删除不属于自己实训的教师!");
        }
        log.info("校验通过");
        log.info("正在删除");
        int deletenum = teacherDao.deleteTeacherByTeacherId(teacherId);
        log.info("删除成功");
        log.info("");
        return ResponseResult.SUCCESS("删除教师成功！").setData(deletenum);
    }

    /**
     * 查询单个教师，不含密码
     *
     * @param teacherId
     * @return
     */
    @Override
    public ResponseResult findByIdWithoutPassword(String teacherId) {
        //检查学生是否存在
        if(teacherDao.findTeacherByTeacherId(teacherId) == null){
            return ResponseResult.FAILED("查询的教师不存在!");
        }
        Map<String,Object> result = teacherDao.findByIdWithoutPassword(teacherId);
        log.info("查询成功");
        log.info("");
        return ResponseResult.SUCCESS("查询成功！").setData(result);
    }


    private Teacher parseByTokenKey(String tokenKey){
        //通过cookie传来的tokenkey，和KEY_TOKEN组合后，从redis中取出真正的token
        String token = (String) redisUtils.get(Constants.User.KEY_TOKEN + tokenKey);
        if(token != null){
            try{
                //解析token，生成claims
                Claims claims = JwtUtil.parseJWT(token);
                //返回claim对应的admin
                return ClaimsUtils.claimsToteacher(claims);
            }catch (Exception e){
                log.info("无法解析为教师");
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
