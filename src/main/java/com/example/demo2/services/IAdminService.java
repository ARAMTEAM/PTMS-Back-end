package com.example.demo2.services;

import com.example.demo2.pojo.Admin;
import com.example.demo2.response.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IAdminService {

    ResponseResult initManagerAccount(Admin admin);

    ResponseResult doLogin(Admin admin, HttpServletRequest request, HttpServletResponse response);

    Admin checkAdmin(HttpServletRequest request,HttpServletResponse response);

    ResponseResult doLogout(HttpServletRequest request,HttpServletResponse response);
}
