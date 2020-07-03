package com.example.demo2.controller.training;

import com.example.demo2.pojo.Training;
import com.example.demo2.response.ResponseResult;
import com.example.demo2.services.TrainingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/training")
public class TrainingApi {

    @Autowired
    private TrainingService trainingService;


    /**
     * 根据教务ID，分页查找出他开设的所有实训
     *
     * @param
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 2")
    @GetMapping("/All/{page}")
    public ResponseResult listAllTraingByJiaowu(@PathVariable("page")Integer page,
                                                HttpServletRequest request,
                                                HttpServletResponse response){
        return trainingService.listAllTrainingByJiaowu(page,request,response);
    }

    /**
     * 根据教务ID，不分页查找出他开设的所有实训
     *
     * @param
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 2")
    @GetMapping("/All")
    public ResponseResult listAllTraingByJiaowu(HttpServletRequest request,
                                                HttpServletResponse response){
        return trainingService.listAllTrainingByJiaowuNoPage(request,response);
    }

    /**
     * 查询单个实训的信息
     *
     * @param trainingId
     * @return
     */
    @GetMapping("/One/{trainingId}")
    public ResponseResult listOneTraining(@PathVariable("trainingId")Integer trainingId){
        return trainingService.listOneTraining(trainingId);
    }



    /**
     * 添加实训，需要从请求中获取登录的教务ID
     *
     * @param training
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 2")
    @PostMapping
    public ResponseResult addOneTraining(@RequestBody Training training,
                                         HttpServletRequest request,
                                         HttpServletResponse response){
        return trainingService.addOneTraining(training,request,response);
    }

    /**
     * 更新实训，需要从请求中获取登录的教务ID
     *
     * @param training
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 2")
    @PutMapping
    public ResponseResult updateTraining(@RequestBody Training training,
                                         HttpServletRequest request,
                                         HttpServletResponse response){
        return trainingService.updateTraining(training,request,response);
    }

    /**
     * 删除实训，需要教务登录，并且只能删除自己的实训
     *
     * @param trainingId
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 2")
    @DeleteMapping("{trainingId}")
    public ResponseResult deleteTraining(@PathVariable("trainingId")Integer trainingId,
                                         HttpServletRequest request,
                                         HttpServletResponse response){
        return trainingService.deleteTraining(trainingId,request,response);
    }
}
