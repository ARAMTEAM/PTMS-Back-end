package com.example.demo2.services;

import com.example.demo2.pojo.Expectation;
import com.example.demo2.response.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ExpectationService {
    ResponseResult listAllE(int trainingId,int page, HttpServletRequest request, HttpServletResponse response);

    ResponseResult JiaowulistOneE(String studentId, HttpServletRequest request, HttpServletResponse response);

    ResponseResult listOneE( String studentId, HttpServletRequest request, HttpServletResponse response);

    ResponseResult addOneE(Expectation expectation, HttpServletRequest request, HttpServletResponse response);

    ResponseResult allotE(int trainingId, HttpServletRequest request, HttpServletResponse response);


}
