package com.example.demo2.services;

import com.example.demo2.pojo.Student;
import com.example.demo2.response.ResponseResult;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface StudentService {

    ResponseResult sutdentLogin(Student student, HttpServletRequest request, HttpServletResponse response);

    Student checkStudent(HttpServletRequest request,HttpServletResponse response);


    ResponseResult listAllStudent(HttpServletRequest request,
                                            HttpServletResponse response,
                                            Integer page);

    ResponseResult listAllStudentBytrainingId(int trainingId,
                                              int page,
                                              HttpServletRequest request,
                                              HttpServletResponse response);
    ResponseResult listAllStudentByProjectId(int projectId,
                                             int page,
                                             HttpServletRequest request,
                                             HttpServletResponse response);
    ResponseResult listAllStudentByTeacherId(HttpServletRequest request, HttpServletResponse response);

    ResponseResult addStudentWithTraining(@RequestBody Student student,
                                          HttpServletRequest request,
                                          HttpServletResponse response);

    ResponseResult updateStudent(Student student, HttpServletRequest request, HttpServletResponse response);

    ResponseResult deleteStudent(String studentId, HttpServletRequest request, HttpServletResponse response);


    ResponseResult resetFirstPassword(String studentId,
                           String password,
                           HttpServletRequest request,
                           HttpServletResponse response);


    ResponseResult findOneByIdWithoutPassword(String studentId);

    ResponseResult studentLogout();


    ResponseResult ListNoEStudent(int trainingId,int page,HttpServletRequest request, HttpServletResponse response);

    ResponseResult getAllNum();
}
