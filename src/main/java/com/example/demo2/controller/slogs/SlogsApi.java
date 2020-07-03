package com.example.demo2.controller.slogs;

import com.example.demo2.response.ResponseResult;
import com.example.demo2.services.SlogsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/slogs")
public class SlogsApi {

    @Autowired
    private SlogsService slogsService;

    @PreAuthorize("@permission.CheckUser() == 1")
    @GetMapping("/{page}")
    public ResponseResult ListAllLogs(@PathVariable("page")int page,
                                      HttpServletRequest request,
                                      HttpServletResponse response){
        return slogsService.listAllLogs(page,request,response);
    }



}
