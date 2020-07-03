package com.example.demo2.controller.trainingnotice;

import com.example.demo2.pojo.Trainingnotice;
import com.example.demo2.response.ResponseResult;
import com.example.demo2.services.TrainingnoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/jiaowunotice")
public class TrainingnoticeApi {

    @Autowired
    private TrainingnoticeService trainingnoticeService;

    /**
     * 分页列出某个实训的公告
     *
     * @param trainingId
     * @param page
     * @return
     */
    @PreAuthorize("@permission.CheckUser() >= 1")
    @GetMapping("/{trainingId}/{page}")
    public ResponseResult listByJiaowuId(@PathVariable("trainingId")String trainingId,
                                         @PathVariable("page")int page){
        return trainingnoticeService.listByJiaowuId(trainingId,page);
    }

    /**
     * 教务发布实训公告
     *
     * @param trainingnotice
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 2")
    @PostMapping
    public ResponseResult addNotice(@RequestBody Trainingnotice trainingnotice,
                                    HttpServletRequest request,
                                    HttpServletResponse response){
        return trainingnoticeService.addNotice(trainingnotice,request,response);
    }

    /**
     * 更新公告
     *
     * @param trainingnotice
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 2")
    @PutMapping
    public ResponseResult updateNotice(@RequestBody Trainingnotice trainingnotice,
                                       HttpServletRequest request,
                                       HttpServletResponse response){
        return trainingnoticeService.updateNotice(trainingnotice,request,response);
    }

    /**
     * 删除公告
     *
     * @param trainingnoticeId
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 2")
    @DeleteMapping("/{trainingnoticeId}")
    public ResponseResult deleteNotice(@PathVariable("trainingnoticeId")int trainingnoticeId,
                                 HttpServletRequest request,
                                 HttpServletResponse response){
        return trainingnoticeService.deleteNotice(trainingnoticeId,request,response);
    }

}
