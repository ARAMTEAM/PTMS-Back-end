package com.example.demo2.controller.student;

import com.example.demo2.pojo.Student;
import com.example.demo2.response.ResponseResult;
import com.example.demo2.services.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/student")
public class StudentApi {
    @Autowired
    private StudentService studentService;


    /**
     * 学生登录
     *
     * @param student
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/login")
    public ResponseResult studentLogin(@RequestBody Student student,
                                       HttpServletRequest request,
                                       HttpServletResponse response){
        return studentService.sutdentLogin(student,request,response);
    }

    /**
     * 退出登录
     *
     * @return
     */
    @GetMapping("/logout")
    public ResponseResult logout(){
        return studentService.studentLogout();
    }


    /**
     * 查询出某个学生所有实训中所有学生除了密码外的信息
     *
     * @param page
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 2")
    @GetMapping("/All/{page}")
    public ResponseResult listAllStudent(@PathVariable("page") int page,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response){
        return studentService.listAllStudent(request,response,page);
    }

    @PreAuthorize("@permission.CheckUser() == 1")
    @GetMapping("/sunNum")
    public ResponseResult listStudentNum(){
        return studentService.getAllNum();
    }

    /**
     * 查询某个实训中的所有学生
     *
     * @param trainingId
     * @param page
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 2")
    @GetMapping("/AllInOneTraining/{trainingId}/{page}")
    public ResponseResult listAllStudentBytrainingId(@PathVariable("trainingId")int trainingId,
                                                     @PathVariable("page")int page,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response){
        return studentService.listAllStudentBytrainingId(trainingId,page,request,response);
    }

    /**
     * 列出某个项目下的所有学生信息
     *
     * @param projectId
     * @param page
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() >= 1")
    @GetMapping("/AllInOneProject/{projectId}/{page}")
    public ResponseResult listAllStudentByProjectId(@PathVariable("projectId")int projectId,
                                                    @PathVariable("page")int page,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response){
        return studentService.listAllStudentByProjectId(projectId,page,request,response);
    }

    /**
     * 列出某个老师下的所有学生信息
     *
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() >= 1")
    @GetMapping("/AllInOneTeacher")
    public ResponseResult listAllStudentByTeacherId(HttpServletRequest request,
                                                    HttpServletResponse response){
        return studentService.listAllStudentByTeacherId(request,response);
    }

    /**
     * 没报志愿，没报创新
     *
     * @param trainingId
     * @param page
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() >= 1")
    @GetMapping("/NoE//{trainingId}/{page}")
    public ResponseResult ListNoEStudent(@PathVariable("trainingId")int trainingId,
                                            @PathVariable("page")int page,
                                            HttpServletRequest request,
                                            HttpServletResponse response){
        return studentService.ListNoEStudent(trainingId,page,request,response);
    }




    /**
     * 查询单个学生的信息，不含密码
     *
     * @param studentId
     * @return
     */
    @PreAuthorize("@permission.CheckUser() >= 1")
    @GetMapping("/One/{studentId}")
    public ResponseResult listOneStudentNopasssword(@PathVariable("studentId")String studentId){
        return studentService.findOneByIdWithoutPassword(studentId);
    }

    /**
     * 增加学生到实训
     *
     * @param student
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 2")
    @PostMapping
    public ResponseResult addStudentWithTraining(@RequestBody Student student,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response){
        return studentService.addStudentWithTraining(student,request,response);
    }

    /**
     * 更新学生信息
     *
     * @param student
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 2")
    @PutMapping
    public ResponseResult updateStudent(@RequestBody Student student,
                                        HttpServletRequest request,
                                        HttpServletResponse response){
        return studentService.updateStudent(student,request,response);
    }

    /**
     * 删除学生，根据学号
     *
     * @param studentId
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 2")
    @DeleteMapping("/{studentId}")
    public ResponseResult deleteStudent(@PathVariable("studentId")String studentId,
                                        HttpServletRequest request,
                                        HttpServletResponse response){
        return studentService.deleteStudent(studentId,request,response);
    }

    /**
     * 学生重置初始密码
     *
     * @param studentId
     * @param password
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 3")
    @PostMapping("/resetFirstPassword/{studentId}/{password}")
    public ResponseResult resetFirstPassword(@PathVariable("studentId")String studentId,
                                             @PathVariable("password")String password,
                                             HttpServletRequest request,
                                             HttpServletResponse response){
        return studentService.resetFirstPassword(studentId,password,request,response);
    }




}
