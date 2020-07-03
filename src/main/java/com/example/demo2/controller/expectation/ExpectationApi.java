package com.example.demo2.controller.expectation;

import com.example.demo2.pojo.Expectation;
import com.example.demo2.response.ResponseResult;
import com.example.demo2.services.ExpectationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/expectation")
public class ExpectationApi {

    @Autowired
    private ExpectationService expectationService;

    /**
     * 分页列出当前实训下所有的学生志愿
     *
     * @param trainingId
     * @param page
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 2")
    @GetMapping("/All/{trainingId}/{page}")
    public ResponseResult listAllE(@PathVariable("trainingId")int trainingId,
                                   @PathVariable("page")int page,
                                   HttpServletRequest request,
                                   HttpServletResponse response){
        return expectationService.listAllE(trainingId,page,request,response);
    }



    /**
     * 教务查询单个学生志愿
     *
     * @param studentId
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 2 ")
    @GetMapping("/OneJS/{studentId}")
    public ResponseResult JiaowulistOneE(@PathVariable("studentId")String studentId,
                                   HttpServletRequest request,
                                   HttpServletResponse response){
        return expectationService.JiaowulistOneE(studentId,request,response);
    }


    /**
     * 学生列出自己的志愿
     *
     * @param studentId
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 3 ")
    @GetMapping("/OneSS/{studentId}")
    public ResponseResult StudentlistOneE(@PathVariable("studentId")String studentId,
                                    HttpServletRequest request,
                                    HttpServletResponse response){
        return expectationService.listOneE(studentId,request,response);
    }

    /**
     * 学生报志愿
     *
     * @param expectation
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 3")
    @PostMapping()
    public ResponseResult addOneE(@RequestBody Expectation expectation,
                                  HttpServletRequest request,
                                  HttpServletResponse response){
        return expectationService.addOneE(expectation,request,response);
    }

    /**
     * 分配志愿
     *
     * @param trainingId
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 2")
    @PostMapping("/allot/{trainingId}")
    public ResponseResult allotE(@PathVariable("trainingId")int trainingId,
                                 HttpServletRequest request,
                                 HttpServletResponse response){
        return expectationService.allotE(trainingId,request,response);
    }

}
