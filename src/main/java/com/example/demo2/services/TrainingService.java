package com.example.demo2.services;

import com.example.demo2.pojo.Jiaowu;
import com.example.demo2.pojo.Training;
import com.example.demo2.response.ResponseResult;
import org.springframework.data.jpa.repository.Query;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TrainingService {


    ResponseResult listAllTrainingByJiaowu(Integer page,
                                           HttpServletRequest request,
                                           HttpServletResponse response);

    ResponseResult listOneTraining(Integer trainingID);

    ResponseResult addOneTraining(Training training,
                                  HttpServletRequest request,
                                  HttpServletResponse response);

    ResponseResult updateTraining(Training training,
                                  HttpServletRequest request,
                                  HttpServletResponse response);

    ResponseResult deleteTraining(Integer trainingId,
                                  HttpServletRequest request,
                                  HttpServletResponse response);

    ResponseResult listAllTrainingByJiaowuNoPage(HttpServletRequest request, HttpServletResponse response);
}
