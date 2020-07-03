package com.example.demo2.services.impl;

import com.example.demo2.dao.RefreshTokenDao;
import com.example.demo2.dao.StudentDao;
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
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentDao studentDao;
    @Autowired
    private TrainingDao trainingDao;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private JiaowuService jiaowuService;
    @Autowired
    private RefreshTokenDao refreshTokenDao;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private TeacherService teacherService;


    /**
     * 登陆方法
     *
     * @param student
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult sutdentLogin(Student student, HttpServletRequest request, HttpServletResponse response) {
        String student_id = student.getStudentId();
        String password = student.getStudentPassword();
        if(TextUtils.isEmpty(student_id)||TextUtils.isEmpty(password)){
            return ResponseResult.FAILED("账号或密码不可以为空!");
        }
        Student studentById = studentDao.findStudentByStudentId(student_id);
        if(studentById == null){
            return ResponseResult.FAILED("账号或密码错误!");
        }
        //还未修改默认密码
        if(studentById.getStudentPassword().equals("000000")){
            if(password.equals("000000")){
                createStudentToken(response,studentById);
                return ResponseResult.SUCCESS("登录成功!");
            }else{
                return ResponseResult.FAILED("账号或密码错误！");
            }
        }else{
            if(!bCryptPasswordEncoder.matches(password,studentById.getStudentPassword())){
                return ResponseResult.FAILED("账号或密码错误！");
            }else{
                createStudentToken(response,studentById);
                return ResponseResult.SUCCESS("登录成功!").setData(student_id);
            }
        }

    }

    /**
     * 检查学生登录
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    public Student checkStudent(HttpServletRequest request, HttpServletResponse response) {
        String tokenKey = CookieUtils.getCookie(request,Constants.User.COOKIE_TOKE_KEY);
        Student student = parseByTokenKey(tokenKey);
        if(student == null){
            RefreshToken refreshToken = refreshTokenDao.findOneByTokenKey(tokenKey);
            if(refreshToken == null){
                return null;
            }
            try{
                JwtUtil.parseJWT(refreshToken.getRefreshToken());
                String userId = refreshToken.getUserId();
                Student studentById = studentDao.findStudentByStudentId(userId);
                log.info("创建新的token和refreshtoken...");
                String newTokenKey = createStudentToken(response,studentById);
                return parseByTokenKey(newTokenKey);
            }catch (Exception e){
                log.info("refreshToken 过期...");
                //如果refreshtoken过期，当前访问未登录，提示用户登录
                return null;
            }
        }
        return student;
    }



    /**
     * 生成token
     *
     * @param response
     * @param studentById
     * @return
     */
    private String createStudentToken(HttpServletResponse response, Student studentById) {
        log.info("学生登录成功，开始创建token……");
        int deleteResult = refreshTokenDao.deleteAllByUserIdAndUserType(studentById.getStudentId(),Constants.User.UserType_student);
        log.info("the num of deleteed results is == > "+deleteResult);
        Map<String,Object> claims = ClaimsUtils.studentToclaims(studentById);
        String token = JwtUtil.createToken(claims);
        String tokenKey = DigestUtils.md5DigestAsHex(token.getBytes());
        redisUtils.set(Constants.User.KEY_TOKEN + tokenKey,token,Constants.TimeValue.HOUR_2);
        CookieUtils.setUpCookie(response,Constants.User.COOKIE_TOKE_KEY,tokenKey);
        String refreshtokenvalue = JwtUtil.createRefreshToken(studentById.getStudentId(),Constants.TimeValue.DAY);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(refreshtokenvalue);
        refreshToken.setUserId(studentById.getStudentId());
        refreshToken.setUserType(Constants.User.UserType_student);
        refreshToken.setTokenKey(tokenKey);
        refreshToken.setCreateTime(new Date());
        refreshToken.setUpdateTime(new Date());
        refreshTokenDao.save(refreshToken);
        //返回token的md5值
        return tokenKey;
    }

    /**
     * 列出当前教务下所有的学生
     *
     * @param request
     * @param response
     * @param page
     * @return
     */
    @Override
    public ResponseResult listAllStudent(HttpServletRequest request, HttpServletResponse response,Integer page) {
        //检查教务是否登录，需要教务权限
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(jiaowu == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(jiaowu.getJiaowuId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        String jiaowuId = jiaowu.getJiaowuId();
        log.info("教务  "+jiaowuId+"  正在查询自己所有实训中的所有学生");
        Pageable pageable = PageRequest.of(page -1, Constants.Page.DEFAUT_PAGE_SIZE);
        Page<List<Map<String,Object>>> all = studentDao.listallWithoutpassword(jiaowuId,pageable);
        log.info("查询成功");
        return ResponseResult.SUCCESS("查询成功！").setData(all);
    }

    /**
     * 查出某个实训中的全部学生
     *
     * @param trainingId
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult listAllStudentBytrainingId(int trainingId, int page,HttpServletRequest request, HttpServletResponse response) {
        //检查教务是否登录，需要教务权限
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(jiaowu == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(jiaowu.getJiaowuId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        String jiaowuId = jiaowu.getJiaowuId();
        log.info("教务  "+jiaowuId+"  正在查询实训  "+trainingId+"  中的所有学生");
        //找出这个实训所属的教务
        String jiaowuIdBytrainingId = trainingDao.findByTrainingId(trainingId).getJiaowuId();
        log.info("这个实训所属的教务是  "+jiaowuIdBytrainingId);
        //判断是否为同一个教务
        if(!jiaowuId.equals(jiaowuIdBytrainingId)){
            return ResponseResult.FAILED("不能查询不属于自己的实训中的学生！");
        }
        //校验通过，开始查询
        log.info("校验通过");
        Pageable pageable = PageRequest.of(page -1, Constants.Page.DEFAUT_PAGE_SIZE);
        Page<List<Map<String,Object>>> result = studentDao.listAllStudentBytrainingId(trainingId,pageable);
        log.info("查询成功");
        return ResponseResult.SUCCESS("查询成功！").setData(result);
    }


    /**
     * 列出某个项目下所有的学生信息
     *
     * @param projectId
     * @param page
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult listAllStudentByProjectId(int projectId, int page, HttpServletRequest request, HttpServletResponse response) {
        log.info("查询项目  "+projectId+"  中的所有学生信息");
        Pageable pageable = PageRequest.of(page -1, Constants.Page.DEFAUT_PAGE_SIZE);
        Page<List<Map<String,Object>>> result = studentDao.listAllStudentByProjectId(projectId,pageable);
        log.info("查询成功");
        return ResponseResult.SUCCESS("查询成功").setData(result);
    }

    /**
     * 列出某教师下所有学生的信息
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult listAllStudentByTeacherId(HttpServletRequest request, HttpServletResponse response) {
        Teacher teacher =teacherService.checkTeacher(request,response);
        if(teacher == null || teacher.getTeacherId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("教师  "+teacher.getTeacherId()+"  正在查询他名下的所有学生");
        List<Map<String,Object>> result = studentDao.listAllStudentByTeacherId(teacher.getTeacherId());
        log.info("查询成功");
        return ResponseResult.SUCCESS("查询成功").setData(result);
    }

    /**
     * 增加学生到某个实训
     *
     * @param student
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult addStudentWithTraining(Student student, HttpServletRequest request, HttpServletResponse response) {
        //检查教务是否登录，需要教务权限
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(jiaowu == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(jiaowu.getJiaowuId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        String jiaowuId = jiaowu.getJiaowuId();
        int trainingId = student.getTrainingId();
        log.info("教务  "+jiaowuId+"  正在向实训  "+trainingId+"  中添加学生");
        String jiaowuIdBytrainingId = trainingDao.findByTrainingId(trainingId).getJiaowuId();
        if(!jiaowuId.equals(jiaowuIdBytrainingId)){
            return ResponseResult.FAILED("不能向不属于自己的实训中增加学生！");
        }
        log.info("登录校验通过，开始增加学生****************");
        student.setStudentPassword("000000");
        log.info("正在添加");
        studentDao.save(student);
        log.info("添加成功");
        return ResponseResult.SUCCESS("增加学生成功！");
    }

    /**
     * 更新学生信息
     *
     * @param student
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult updateStudent(Student student, HttpServletRequest request, HttpServletResponse response) {
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(jiaowu == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(jiaowu.getJiaowuId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("教务  "+jiaowu.getJiaowuId()+"  正在更新学生  "+student.getStudentId()+"  的信息");
        //检查学生是否存在
        if(studentDao.findStudentByStudentId(student.getStudentId()) == null){
            return ResponseResult.FAILED("更新的学生不存在！");
        }
        String jiaowuId = jiaowu.getJiaowuId();
        int trainingId = student.getTrainingId();
        Training trainingBystudent = trainingDao.findByTrainingId(student.getTrainingId());
        if(trainingBystudent == null){
            return ResponseResult.FAILED("实训ID不存在");
        }
        String jiaowuIdBytrainingId = trainingDao.findByTrainingId(trainingId).getJiaowuId();
        if(!jiaowuId.equals(jiaowuIdBytrainingId)){
            return ResponseResult.FAILED("不能更新不属于自己实训中学生的信息！");
        }
        log.info("校验通过");
        Student studentById = studentDao.findStudentByStudentId(student.getStudentId());
        //判断是否需要更新密码
        if(student.getStudentPassword() != null){
            String password = student.getStudentPassword();
            if(TextUtils.isLegal(password)){
                String encode = bCryptPasswordEncoder.encode(password);
                student.setStudentPassword(encode);
                log.info("更新学生密码  "+encode);
            }else{
                return ResponseResult.FAILED("密码长度必须不少于6位字符");
            }
        }
        //检查需要更新的字段
        BeanUtils.copyProperties(student, studentById, getNullPropertyNames(student));
        //存入数据库


        log.info("正在更新");
        studentDao.save(studentById);
        log.info("更新成功");
        return ResponseResult.SUCCESS("更新学生信息成功！");
    }

    /**
     * 删除学生
     *
     * @param studentId
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult deleteStudent(String studentId, HttpServletRequest request, HttpServletResponse response) {
        //需要教务权限
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(jiaowu == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(jiaowu.getJiaowuId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        String jiaowuId = jiaowu.getJiaowuId();
        log.info("教务  "+jiaowuId+"  正在删除学生  "+studentId+"  的信息");
        //检查学生是否存在
        if(studentDao.findStudentByStudentId(studentId) == null){
            return ResponseResult.FAILED("删除的学生不存在！");
        }
        Student studentById = studentDao.findStudentByStudentId(studentId);
        String jiaowuIdBytrainingId = trainingDao.findByTrainingId(studentById.getTrainingId()).getJiaowuId();
        if(!jiaowuId.equals(jiaowuIdBytrainingId)){
            return ResponseResult.FAILED("不能删除不属于自己实训中学生的信息！");
        }
        log.info("正在删除");
        int deletenum = studentDao.deleteAllByStudentId(studentId);
        log.info("删除成功");
        return ResponseResult.SUCCESS("删除学生成功！").setData(deletenum);
    }

    /**
     * 让学生修改自己的默认密码000000
     *
     * @param studentId
     * @param password
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult resetFirstPassword(String studentId, String password,HttpServletRequest request, HttpServletResponse response) {
        //检查权限，是否是学生登陆，并且学生ID要和修改的ID对应
        log.info("学生  "+studentId+"  正在修改初始密码");
        Student student = checkStudent(request,response);
        if(student == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(student.getStudentId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //检验当前登录的学生ID和要修改的学生ID是否一直
        Student studentById = studentDao.findStudentByStudentId(studentId);
        if(!studentById.getStudentId().equals(student.getStudentId())){
            log.info("不能修改别人的密码！!！");
            return ResponseResult.FAILED("操作有误！！");
        }
        if(!studentById.getStudentPassword().equals("000000")){
            log.info("学生  "+studentId+"  的密码不是默认值，不能重置");
            return ResponseResult.FAILED("密码不是默认值，不能重置，请联系教务！！");
        }
        log.info("校验完成，正在修改初始密码");
        //加密密码
        String encode = bCryptPasswordEncoder.encode(password);
        studentById.setStudentPassword(encode);
        studentDao.save(studentById);
        log.info("修改初始密码完成");
        return ResponseResult.SUCCESS("修改成功！");

    }


    /**
     * 查询单个学生的信息
     *
     * @param studentId
     * @return
     */
    @Override
    public ResponseResult findOneByIdWithoutPassword(String studentId) {
        if(studentDao.findStudentByStudentId(studentId) == null){
            return ResponseResult.FAILED("查询的学生不存在！");
        }
        log.info("查询学生  "+studentId+"  的信息");
        Map<String,Object> result = studentDao.findOneByIdWithoutPassword(studentId);
        log.info("查询成功");
        return ResponseResult.SUCCESS("查询成功！").setData(result);
    }

    /**
     * 学生退出登录
     *
     * @return
     */
    @Override
    public ResponseResult studentLogout() {
        log.info("学生开始执行退出登录");
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
     * 查询出没有填报志愿并且没报创新实训的学生信息
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult ListNoEStudent(int trainingId,int page,HttpServletRequest request, HttpServletResponse response) {
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(jiaowu == null || jiaowu.getJiaowuId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("教务  "+jiaowu.getJiaowuId()+"  正在查询出没有填报志愿并且没报创新实训的学生信息");
        Training training = trainingDao.findByTrainingId(trainingId);
        if(!training.getJiaowuId().equals(jiaowu.getJiaowuId())){
            return ResponseResult.FAILED("无权操作");
        }
        log.info("校验通过");
        Pageable pageable = PageRequest.of(page - 1,Constants.Page.DEFAUT_PAGE_SIZE);
        Page<List<Map<String,Object>>> result = studentDao.ListNoEStudent(trainingId,pageable);
        log.info("查询成功");
        return ResponseResult.SUCCESS("查询成功").setData(result);

    }

    /**
     * 获取总数量
     *
     * @return
     */
    @Override
    public ResponseResult getAllNum() {
        return ResponseResult.SUCCESS("查询成功").setData(studentDao.getStudentNum());
    }

    /**
     * 解析token
     *
     * @param tokenKey
     * @return
     */
    private Student parseByTokenKey(String tokenKey){
        //通过cookie传来的tokenkey，和KEY_TOKEN组合后，从redis中取出真正的token
        String token = (String) redisUtils.get(Constants.User.KEY_TOKEN + tokenKey);
        if(token != null){
            try{
                //解析token，生成claims
                Claims claims = JwtUtil.parseJWT(token);
                //返回claim对应的admin
                return ClaimsUtils.claimsTostudent(claims);
            }catch (Exception e){
                log.info("无法解析为学生");
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
