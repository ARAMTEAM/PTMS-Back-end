package com.example.demo2.services.impl;

import com.example.demo2.pojo.Admin;
import com.example.demo2.pojo.Jiaowu;
import com.example.demo2.pojo.Student;
import com.example.demo2.pojo.Teacher;
import com.example.demo2.services.IAdminService;
import com.example.demo2.services.JiaowuService;
import com.example.demo2.services.StudentService;
import com.example.demo2.services.TeacherService;
import com.example.demo2.utils.Constants;
import com.example.demo2.utils.CookieUtils;
import com.example.demo2.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service("permission")
public class PermissionService {

    @Autowired
    private IAdminService adminService;
    @Autowired
    private JiaowuService jiaowuService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudentService studentService;

    /**
     * 判断是什么用户登录
     * 0:没有登陆
     * 1:管理员
     * 2:教务
     * 3:教师
     * 4:学生
     *
     * @return
     */
    public int CheckUser(){
        //拿到request和response
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();
        String tokenKey = CookieUtils.getCookie(request, Constants.User.COOKIE_TOKE_KEY);
        //没有cookie，肯定没有登录，不用继续了
        if(TextUtils.isEmpty(tokenKey)){
            log.info("tokenKey为空,没有登陆");
            return 0;
        }
        //这里应该是四种角色判断都为空
        Admin admin = adminService.checkAdmin(request,response);
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        Student student = studentService.checkStudent(request,response);
        Teacher teacher = teacherService.checkTeacher(request,response);

        if(admin == null && jiaowu == null && student == null && teacher == null){
            log.info("检查到没有任何用户登录，拒绝访问");
            return 0;
        }
        if(admin.getAdminId() != null && jiaowu.getJiaowuId() == null && student.getStudentId() == null && teacher.getTeacherId() == null){
            log.info("当前登录用户为: 管理员 ********"+admin.getAdminId());
            return 1;
        }
        if(admin.getAdminId() == null && jiaowu.getJiaowuId() != null && student.getStudentId() == null && teacher.getTeacherId() == null){
            log.info("当前登录用户为: 教务 ********"+jiaowu.getJiaowuId());
            return 2;
        }
        if(admin.getAdminId() == null && jiaowu.getJiaowuId() == null && student.getStudentId() != null && teacher.getTeacherId() == null){
            log.info("当前登录用户为: 学生 ********"+student.getStudentId());
            return 3;
        }
        if(admin.getAdminId() == null && jiaowu.getJiaowuId() == null && student.getStudentId() == null && teacher.getTeacherId() != null){
            log.info("当前登录用户为: 教师 ********"+teacher.getTeacherId());
            return 4;
        }
        return 0;
    }
}
