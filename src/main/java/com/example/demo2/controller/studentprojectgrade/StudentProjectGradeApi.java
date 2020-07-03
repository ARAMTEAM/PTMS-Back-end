package com.example.demo2.controller.studentprojectgrade;

import com.example.demo2.pojo.StudentProjectGrade;
import com.example.demo2.response.ResponseResult;
import com.example.demo2.services.StudentProjectGradeService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.PolicySpi;

@Slf4j
@RestController
@RequestMapping("/stuAndpro")
public class StudentProjectGradeApi {

    @Autowired
    StudentProjectGradeService studentProjectGradeService;

    /*
    添加
     */

    /**
     * 学生添加
     *
     * @param spjg
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 3")
    @PostMapping("/studentAdd")
    public ResponseResult StudentAdd(@RequestBody StudentProjectGrade spjg,
                                     HttpServletRequest request,
                                     HttpServletResponse response){
        return studentProjectGradeService.StudentAdd(spjg,request,response);
    }

    /**
     * 教师添加
     *
     * @param spjg
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 4")
    @PostMapping("/teacherAdd")
    public ResponseResult TeacherAdd(@RequestBody StudentProjectGrade spjg,
                                     HttpServletRequest request,
                                     HttpServletResponse response){
        return studentProjectGradeService.TeacherAdd(spjg,request,response);
    }

    /**
     * 教务添加
     *
     * @param spjg
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 2")
    @PostMapping("/jiaowuAdd")
    public ResponseResult JiaowuAdd(@RequestBody StudentProjectGrade spjg,
                                    HttpServletRequest request,
                                    HttpServletResponse response){
        return studentProjectGradeService.JiaowuAdd(spjg,request,response);
    }

    /*
    删除
     */

    /**
     * 学生拒绝入队的邀请
     *
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 3")
    @DeleteMapping("/studentReject")
    public ResponseResult studentReject(HttpServletRequest request,
                                        HttpServletResponse response){
        return studentProjectGradeService.studentReject(request,response);
    }


    /**
     * 教务删除
     *
     * @param studentId
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 2")
    @DeleteMapping("/jiaowuDeleete/{studentId}")
    public ResponseResult jiaowuDelete(@PathVariable("studentId")String studentId,
                                        HttpServletRequest request,
                                        HttpServletResponse response) {
        return studentProjectGradeService.jiaowuDelete(studentId, request, response);
    }

    /*
    查询
     */

    /**
     * 列出某一项目下的学生
     *
     * @param projectId
     * @return
     */
    @PreAuthorize("@permission.CheckUser() >= 1")
    @GetMapping("/listByProjectId/{projectId}")
    public ResponseResult listAllItemByProjectId(@PathVariable("projectId")Integer projectId){
        return studentProjectGradeService.listAllItemByProjectId(projectId);
    }

    /**
     * 查出某一学生所在项目的信息
     *
     * @param studentId
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() >= 1")
    @GetMapping("/listByStudentId/{studentId}")
    public ResponseResult listOneItemByStudent(@PathVariable("studentId")String studentId,
                                               HttpServletRequest request,
                                               HttpServletResponse response){
        return studentProjectGradeService.listOneItemByStudent(studentId,request,response);
    }

    /**
     * 列出所有不在项目内的学生
     *
     * @param trainingId
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 2")
    @GetMapping("/listNoProjectStudent/{trainingId}/{page}")
    public ResponseResult listNoProjectStudent(@PathVariable("trainingId")int trainingId,
                                               @PathVariable("page")int page,
                                               HttpServletRequest request,
                                               HttpServletResponse response){
        return studentProjectGradeService.listNoProjectStudent(trainingId,page,request,response);
    }

    /**
     * 成绩导出
     *
     * @param trainingId
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 2")
    @GetMapping("/gradeOutput/{trainingId}")
    public ResponseResult GradeOutput(@PathVariable("trainingId")int trainingId,
                                      HttpServletRequest request,
                                      HttpServletResponse response){
        return studentProjectGradeService.GradeOutput(trainingId,request,response);
    }

    /*
    更新
     */

    /**
     * 学生接受入队的邀请
     *
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 3")
    @PostMapping("/studentReceive")
    public ResponseResult studentReceive(HttpServletRequest request,
                                        HttpServletResponse response){
        return studentProjectGradeService.studentReceive(request,response);
    }

    /**
     * 教师更新成绩（百分制）
     *
     * @param studentId
     * @param grade
     * @param request
     * @param response
     * @return
     */@PreAuthorize("@permission.CheckUser() == 4")
    @PutMapping("/update100Grade/{studentId}/{grade}")
    public ResponseResult TeacherUpdate100Grade(@PathVariable("studentId")String studentId,
                                             @PathVariable("grade")int grade,
                                             HttpServletRequest request,
                                             HttpServletResponse response){
        return studentProjectGradeService.TeacherUpdate100Grade(studentId,grade,request,response);
    }

    /**
     * 教师更新成绩（5分制）
     *
     * @param studentId
     * @param grade
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 4")
    @PutMapping("/update5Grade/{studentId}/{grade}")
    public ResponseResult TeacherUpdate5Grade(@PathVariable("studentId")String studentId,
                                                @PathVariable("grade")double grade,
                                                HttpServletRequest request,
                                                HttpServletResponse response){
        return studentProjectGradeService.TeacherUpdate5Grade(studentId,grade,request,response);
    }

    /**
     * 教务更新
     *
     * @param spg
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 2")
    @PutMapping
    public ResponseResult JiaowuUpdate(@RequestBody StudentProjectGrade spg,
                                       HttpServletRequest request,
                                       HttpServletResponse response){
        return studentProjectGradeService.JiaowuUpdate(spg,request,response);
    }



}
