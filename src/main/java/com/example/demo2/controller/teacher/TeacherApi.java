package com.example.demo2.controller.teacher;


import com.example.demo2.pojo.Jiaowu;
import com.example.demo2.pojo.Teacher;
import com.example.demo2.response.ResponseResult;
import com.example.demo2.services.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/teacher")
public class TeacherApi {

    @Autowired
    private TeacherService teacherService;


    /**
     * 教务登录
     *
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/login")
    public ResponseResult teacherlogin(@RequestBody Teacher teacher,
                                      HttpServletRequest request,
                                      HttpServletResponse response){
        return teacherService.TeahcerLogin(teacher,request,response);
    }

    /**
     * 教师退出登录
     *
     * @return
     */
    @GetMapping("/logout")
    public ResponseResult logout(){
        return teacherService.doLogout();

    }


    /**
     * 分页查询当前教务下所有实训的所有老师
     *
     * @param request
     * @param response
     * @param page
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 2")
    @GetMapping("/All/{page}")
    public ResponseResult listAllTeacherByJiaowu(HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 @PathVariable("page")Integer page){
        return teacherService.listAllTeacherWithoutPassword(request,response,page);
    }

    /**
     * 管理员获取教师数量
     *
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 1")
    @GetMapping("/sunNum")
    public ResponseResult listTeacherNum(){
        return teacherService.getSumNum();
    }

    /**
     * 不分页查询当前教务下所有实训的所有老师
     *
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 2")
    @GetMapping("/All")
    public ResponseResult listAllTeacherByJiaowuNoPage(HttpServletRequest request,
                                                 HttpServletResponse response){
        return teacherService.listAllTeacherByJiaowuNoPage(request,response);
    }

    /**
     * 列出可以选择的指导教师
     *
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 3")
    @GetMapping("/CanChoose")
    public ResponseResult listAllTeacherToChoose(HttpServletRequest request,
                                                 HttpServletResponse response){
        return teacherService.listAllTeacherToChoose(request,response);
    }

    /**
     * 查询某一位老师的基本信息
     *
     * @param teacherId
     * @return
     */
    @GetMapping("/One/{teacherId}")
    public ResponseResult listOneTeacherNoPassword(@PathVariable("teacherId")String teacherId){
        return teacherService.findByIdWithoutPassword(teacherId);
    }

    /**
     * 增加教师
     *
     * @param teacher
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 2")
    @PostMapping
    public ResponseResult addTeacher(@RequestBody Teacher teacher,
                                     HttpServletRequest request,
                                     HttpServletResponse response){
        return teacherService.addTeacher(teacher,request,response);
    }

    /**
     * 更新教师
     *
     * @param teacher
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 2")
    @PutMapping
    public ResponseResult updateTeacher(@RequestBody  Teacher teacher,
                                        HttpServletRequest request,
                                        HttpServletResponse response){
        return teacherService.updateStudent(teacher,request,response);
    }

    /**
     * 删除教师，根据教师ID
     *
     * @param teacherId
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 2")
    @DeleteMapping("/{teacherId}")
    public ResponseResult deleteTeacher(@PathVariable("teacherId")String teacherId,
                                        HttpServletRequest request,
                                        HttpServletResponse response){
        return teacherService.deleteStudent(teacherId,request,response);
    }
}
