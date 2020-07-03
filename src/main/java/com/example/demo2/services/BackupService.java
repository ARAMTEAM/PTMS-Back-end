package com.example.demo2.services;

import com.example.demo2.response.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface BackupService {

    ResponseResult queryBackup(HttpServletRequest request, HttpServletResponse response);
    ResponseResult backup(String name, HttpServletRequest request, HttpServletResponse response);
    ResponseResult restore(String name,HttpServletRequest request, HttpServletResponse response);
    ResponseResult delBackup(String name,HttpServletRequest request, HttpServletResponse response);
}
