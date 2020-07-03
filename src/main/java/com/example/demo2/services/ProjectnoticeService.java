package com.example.demo2.services;

import com.example.demo2.pojo.Projectnotice;
import com.example.demo2.response.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ProjectnoticeService {
    ResponseResult listAllnoticeByProjectId(int projectId, int page);

    ResponseResult addNotice(Projectnotice projectnotice, HttpServletRequest request, HttpServletResponse response);

    ResponseResult update(Projectnotice projectnotice, HttpServletRequest request, HttpServletResponse response);

    ResponseResult delete(int projectnoticeId, HttpServletRequest request, HttpServletResponse response);

    ResponseResult listAllnoticeBystudentId(String studentId);
}
