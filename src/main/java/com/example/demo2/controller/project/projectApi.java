package com.example.demo2.controller.project;


import com.example.demo2.pojo.Project;
import com.example.demo2.pojo.Projectnotice;
import com.example.demo2.response.ResponseResult;
import com.example.demo2.services.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/project")
public class projectApi {
    @Autowired
    private ProjectService projectService;


    /*
    查询
     */

    /**
     * 获取当前实训的全部项目
     *
     * @param trainingId
     * @param page
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() >= 1")
    @GetMapping("/All/{trainingId}/{page}")
    public ResponseResult listAllProjectByTrainingId(@PathVariable("trainingId")Integer trainingId,
                                                     @PathVariable("page")Integer page,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response){
        return projectService.listAllProjectByTrainingId(trainingId,page,request,response);
    }

    /**
     * 获取当前实训的全部项目
     *
     * @param trainingId
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() >= 1")
    @GetMapping("/All/{trainingId}")
    public ResponseResult listAllProjectByTrainingIdNoPage(@PathVariable("trainingId")Integer trainingId,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response){
        return projectService.listAllProjectByTrainingIdNoPage(trainingId,request,response);
    }

    /**
     * 列出所有的教师项目,方便学生报志愿
     *
     * @param trainingId
     * @param page
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() >= 1")
    @GetMapping("/teacher/{trainingId}/{page}")
    public ResponseResult listAllTeacherProjectByTrainingId(@PathVariable("trainingId")int trainingId,
                                                     @PathVariable("page")int page,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response){
        return projectService.listAllTeacherProjectByTrainingId(trainingId,page,request,response);
    }

    /**
     * 列出某个教师下的所有项目
     *
     * @param teacherId
     * @return
     */
    @PreAuthorize("@permission.CheckUser() >= 1")
    @GetMapping("/teacher/all/{teacherId}/{page}")
    public ResponseResult listAllByTeacherId(@PathVariable("teacherId")String teacherId,@PathVariable("page")int page){
        return projectService.listAllByTeacherId(teacherId,page);
    }

    /**
     * 列出所有待审核的项目
     *
     * @param page
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 2")
    @GetMapping("/NoAudit/{page}")
    public ResponseResult listAllNoAuditProject(@PathVariable("page")int page,HttpServletRequest request,HttpServletResponse response){
        return projectService.listAllNoAuditProject(page,request,response);
    }

    /**
     * 教师列出他未审核的创新项目
     *
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 4")
    @GetMapping("/NeedReceive")
    public ResponseResult listAllTeacherNeedReceive(HttpServletRequest request,HttpServletResponse response){
        return projectService.listAllTeacherNeedReceive(request,response);
    }

    /**
     * 教务列出所有需答辩
     *
     * @param page
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 2")
    @GetMapping("/NeedDebate/{page}")
    public ResponseResult listAllTeacherNeedReceive(@PathVariable("page")int page,HttpServletRequest request,HttpServletResponse response){
        return projectService.listAllNeedDebateProject(page,request,response);
    }


    /**
     * 列出单个项目的信息
     *
     * @param projectId
     * @return
     */
    @PreAuthorize("@permission.CheckUser() >= 1")
    @GetMapping("/One/{projectId}")
    public ResponseResult listOneByProjectId(@PathVariable("projectId")int projectId){
        return projectService.listOneByProjectId(projectId);
    }


    /*
    添加
     */


    /**
     * 教师申请实训项目
     *
     * @param project
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 4")
    @PostMapping("/teacherAdd")
    public ResponseResult TeacheraddProject(@RequestBody Project project,
                                            HttpServletRequest request,
                                            HttpServletResponse response){
        return projectService.TeacheraddProject(project,request,response);
    }

    /**
     * 学生申请创新实训项目
     *
     * @param project
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 3")
    @PostMapping("/studentAdd")
    public ResponseResult StudentaddProject(@RequestBody Project project,
                                            HttpServletRequest request,
                                            HttpServletResponse response){
        return projectService.StudentaddProject(project,request,response);
    }

    /**
     * 教务添加项目
     *
     * @param project
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 2")
    @PostMapping("/jiaowuAdd")
    public ResponseResult jiaowuAddProject(@RequestBody Project project,
                                           HttpServletRequest request,
                                           HttpServletResponse response){
        return projectService.jiaowuAddProject(project,request,response);
    }


    /*
    更新
     */


    /**
     * 教务更新项目(审核   修改信息  扩充最大人数)
     *
     * @param project
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 2")
    @PutMapping("/jiaowu")
    public ResponseResult JiaowuUpdate(@RequestBody Project project,
                                       HttpServletRequest request,
                                       HttpServletResponse response){
        return projectService.jiaowuUpdate(project,request,response);
    }


    /**
     * 教师更新未通过的项目
     *
     * @param project
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 4")
    @PutMapping("/teacher")
    public ResponseResult TeacherUpdate(@RequestBody Project project,
                                        HttpServletRequest request,
                                        HttpServletResponse response){
        return projectService.TeacherUpdate(project,request,response);
    }

    /**
     * 教师审核学生创新项目邀请
     *
     * @param projectId
     * @param reply
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 4")
    @PostMapping("/verify/{projectId}/{reply}")
    public ResponseResult TeacherReceiveOrNot(@PathVariable("projectId")int projectId,
                                              @PathVariable("reply")int reply,
                                              HttpServletRequest request,
                                              HttpServletResponse response){
        return projectService.TeacherReceiveOrNot(projectId,reply,request,response);
    }


    /*
    删除
     */

    /**
     * 教务删除项目
     *
     * @param projectId
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 2")
    @DeleteMapping("/{projectId}")
    public ResponseResult JiaowuDelete(@PathVariable("projectId") int projectId,
                                       HttpServletRequest request,
                                       HttpServletResponse response){
        return projectService.jiaowuDelete(projectId,request,response);
    }



}
