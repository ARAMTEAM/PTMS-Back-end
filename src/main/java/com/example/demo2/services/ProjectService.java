package com.example.demo2.services;

import com.example.demo2.pojo.Project;
import com.example.demo2.response.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ProjectService {
    ResponseResult listAllProjectByTrainingId(int trainingId, int page, HttpServletRequest request, HttpServletResponse response);

    ResponseResult TeacheraddProject(Project project, HttpServletRequest request, HttpServletResponse response);

    ResponseResult StudentaddProject(Project project, HttpServletRequest request, HttpServletResponse response);


    ResponseResult jiaowuUpdate(Project project, HttpServletRequest request, HttpServletResponse response);

    ResponseResult jiaowuDelete(int projectId ,HttpServletRequest request, HttpServletResponse response);

    ResponseResult listAllByTeacherId(String teacherId,int page);

    ResponseResult listAllTeacherNeedReceive(HttpServletRequest request, HttpServletResponse response);

    ResponseResult listAllTeacherProjectByTrainingId(Integer trainingId, Integer page,HttpServletRequest request, HttpServletResponse response);

    ResponseResult TeacherUpdate(Project project, HttpServletRequest request, HttpServletResponse response);

    ResponseResult TeacherReceiveOrNot(int projectId, int reply,HttpServletRequest request, HttpServletResponse response);


    ResponseResult jiaowuAddProject(Project project, HttpServletRequest request, HttpServletResponse response);

    ResponseResult listAllNoAuditProject(int page,HttpServletRequest request,HttpServletResponse response);

    ResponseResult listAllNeedDebateProject(int page, HttpServletRequest request, HttpServletResponse response);

    ResponseResult listOneByProjectId(int projectId);

    ResponseResult listAllProjectByTrainingIdNoPage(Integer trainingId, HttpServletRequest request, HttpServletResponse response);
}
