package com.example.demo2.services;

import com.example.demo2.pojo.Teacher;
import com.example.demo2.response.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TeacherService {
    ResponseResult listAllTeacherWithoutPassword(HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 Integer page);

    ResponseResult addTeacher(Teacher teacher, HttpServletRequest request, HttpServletResponse response);

    ResponseResult updateStudent(Teacher teacher, HttpServletRequest request, HttpServletResponse response);

    ResponseResult deleteStudent(String teacherId, HttpServletRequest request, HttpServletResponse response);

    ResponseResult findByIdWithoutPassword(String teacherId);

    ResponseResult TeahcerLogin(Teacher teacher, HttpServletRequest request, HttpServletResponse response);
    
    Teacher checkTeacher(HttpServletRequest request,HttpServletResponse response);


    ResponseResult doLogout();

    ResponseResult listAllTeacherByJiaowuNoPage(HttpServletRequest request, HttpServletResponse response );

    ResponseResult listAllTeacherToChoose(HttpServletRequest request, HttpServletResponse response);

    ResponseResult getSumNum();
}
