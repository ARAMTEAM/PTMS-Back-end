package com.example.demo2.services;

import com.example.demo2.pojo.Adminnotice;
import com.example.demo2.response.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AdminnoticeService {

    ResponseResult addAdminnotice(Adminnotice adminnotice,
                                  HttpServletRequest request,
                                  HttpServletResponse response);

    ResponseResult findAllAdminnotice(int page);

    ResponseResult updateAdminnotice(Adminnotice adminnotice ,
                                     HttpServletRequest request ,
                                     HttpServletResponse response);

    ResponseResult deleteAdminnotice(Integer adminnoticeId ,
                                     HttpServletRequest request ,
                                     HttpServletResponse response);
}
