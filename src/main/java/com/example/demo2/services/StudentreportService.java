package com.example.demo2.services;

import com.example.demo2.pojo.Studentreport;
import com.example.demo2.response.ResponseResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface StudentreportService {
    ResponseResult listAllReportByStudentIdToTeacher(String studentId,int page, HttpServletRequest request, HttpServletResponse response);


    ResponseResult listAllReportByStudentIdToStudent(int page, HttpServletRequest request, HttpServletResponse response);

    ResponseResult studentAdd(String title, int type,@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response);


    ResponseResult teacherRead(int studentreportId, HttpServletRequest request, HttpServletResponse response);

    ResponseResult studentDelete(int studentreportId, HttpServletRequest request, HttpServletResponse response);


    String downloadNoPer(HttpServletRequest request,HttpServletResponse response,int studentreportId ) throws IOException;


}
