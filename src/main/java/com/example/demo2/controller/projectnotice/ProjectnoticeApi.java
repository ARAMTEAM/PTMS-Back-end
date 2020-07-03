package com.example.demo2.controller.projectnotice;

import com.example.demo2.pojo.Projectnotice;
import com.example.demo2.response.ResponseResult;
import com.example.demo2.services.ProjectnoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/projectnotice")
public class ProjectnoticeApi {
    @Autowired
    private ProjectnoticeService projectnoticeService;

    /**
     * 分页列出某个项目中的公告
     *
     * @param projectId
     * @param page
     * @return
     */
    @PreAuthorize("@permission.CheckUser() >= 1")
    @GetMapping("/{projectId}/{page}")
    public ResponseResult listAllnoticeByProjectId(@PathVariable("projectId")int projectId,
                                                   @PathVariable("page")int page){
        return projectnoticeService.listAllnoticeByProjectId(projectId,page);
    }

    /**
     * 分页列出某个学生的项目公告
     *
     * @param studentId
     * @return
     */
    @PreAuthorize("@permission.CheckUser() >= 1")
    @GetMapping("/{studentId}")
    public ResponseResult listAllnoticeByStudentId(@PathVariable("studentId")String studentId){
        return projectnoticeService.listAllnoticeBystudentId(studentId);
    }


    /**
     * 教师添加公告
     *
     * @param projectnotice
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 4")
    @PostMapping
    public ResponseResult addNotice(@RequestBody Projectnotice projectnotice,
                                    HttpServletRequest request,
                                    HttpServletResponse response){
        return projectnoticeService.addNotice(projectnotice,request,response);
    }

    /**
     * 教师更新项目公告
     *
     * @param projectnotice
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 4")
    @PutMapping
    public ResponseResult updateNotice(@RequestBody Projectnotice projectnotice,
                                    HttpServletRequest request,
                                    HttpServletResponse response){
        return projectnoticeService.update(projectnotice,request,response);
    }

    /**
     * 教师删除项目公告
     *
     * @param projectnoticeId
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 4")
    @DeleteMapping("/{projectnoticeId}")
    public ResponseResult deleteNotice(@PathVariable("projectnoticeId")int projectnoticeId,
                                       HttpServletRequest request,
                                       HttpServletResponse response){
        return projectnoticeService.delete(projectnoticeId,request,response);
    }





}
