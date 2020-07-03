package com.example.demo2.services;

import com.example.demo2.pojo.Jiaowu;
import com.example.demo2.response.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface JiaowuService {

    ResponseResult JiaowuLogin(Jiaowu jiaowu,HttpServletRequest request,HttpServletResponse response);

    ResponseResult createNewJiaowu(Jiaowu jiaowu,HttpServletRequest request,HttpServletResponse response);

    ResponseResult deleteOneJiaowu(String jiaowuId,HttpServletRequest request,HttpServletResponse response);

    ResponseResult updateOneJiaowu(Jiaowu jiaowu,HttpServletRequest request,HttpServletResponse response);

    ResponseResult findAlljiaowu();

//    Page<Jiaowu> findJiaowuPage(Pageable pageable);
//    Page<List<Map<String,Object>>> findJiaowuPagepart(Pageable pageable);

    ResponseResult listJiaowu(int page, HttpServletRequest request, HttpServletResponse response);

    Jiaowu checkJiaowu(HttpServletRequest request,
                       HttpServletResponse response);

    ResponseResult jiaowuLogout();

    ResponseResult listOneJiaowu(String jiaowuId);
}
