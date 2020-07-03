package com.example.demo2.services;

import com.example.demo2.pojo.StudentProjectGrade;
import com.example.demo2.response.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface StudentProjectGradeService {
    ResponseResult StudentAdd(StudentProjectGrade spjg, HttpServletRequest request, HttpServletResponse response);

    ResponseResult TeacherAdd(StudentProjectGrade spjg, HttpServletRequest request, HttpServletResponse response);

    ResponseResult JiaowuAdd(StudentProjectGrade spjg, HttpServletRequest request, HttpServletResponse response);


    ResponseResult studentReject(HttpServletRequest request, HttpServletResponse response);

    ResponseResult studentReceive(HttpServletRequest request, HttpServletResponse response);

    ResponseResult jiaowuDelete(String studentId,  HttpServletRequest request, HttpServletResponse response);


    ResponseResult listAllItemByProjectId(Integer projectId);

    ResponseResult listOneItemByStudent(String studentId ,HttpServletRequest request, HttpServletResponse response);


    ResponseResult TeacherUpdate100Grade(String studentId, int grade, HttpServletRequest request, HttpServletResponse response);

    ResponseResult TeacherUpdate5Grade(String studentId, double grade, HttpServletRequest request, HttpServletResponse response);


    ResponseResult JiaowuUpdate(StudentProjectGrade spg, HttpServletRequest request, HttpServletResponse response);

    ResponseResult listNoProjectStudent(int trainingId,int page, HttpServletRequest request, HttpServletResponse response);

    ResponseResult GradeOutput(int trainingId, HttpServletRequest request, HttpServletResponse response);
}
