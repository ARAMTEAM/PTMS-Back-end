package com.example.demo2.services;

import com.example.demo2.pojo.Trainingnotice;
import com.example.demo2.response.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TrainingnoticeService {
    ResponseResult listByJiaowuId(String trainingId, int page);

    ResponseResult addNotice(Trainingnotice trainingnotice, HttpServletRequest request, HttpServletResponse response);

    ResponseResult updateNotice(Trainingnotice trainingnotice, HttpServletRequest request, HttpServletResponse response);

    ResponseResult deleteNotice(int trainingnoticeId, HttpServletRequest request, HttpServletResponse response);
}
