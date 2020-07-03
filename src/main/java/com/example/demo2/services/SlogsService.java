package com.example.demo2.services;

import com.example.demo2.response.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SlogsService {
    ResponseResult listAllLogs(int page, HttpServletRequest request, HttpServletResponse response);

    void savelog (String userType,String userId,String operation,String ip);
}
