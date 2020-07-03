package com.example.demo2.controller.admin;

import com.example.demo2.pojo.Admin;
import com.example.demo2.response.ResponseResult;
import com.example.demo2.services.AdminnoticeService;
import com.example.demo2.services.IAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminApi {

    @Autowired
    private IAdminService adminService;


    /**
     * 初始化管理员账号
     * @param admin
     * @return
     */
    @PostMapping("/account")
    public ResponseResult initManageAccount(@RequestBody Admin admin){
        log.info("username ==  >" + admin.getAdminId());
        log.info("password ==  >" + admin.getAdminPassword());
        return adminService.initManagerAccount(admin);
    }

    /**
     * 登录
     *
     * @param admin
     * @param request
     * @param response
     * @return
     */
    @PostMapping("")
    public ResponseResult login(@RequestBody Admin admin,
                                HttpServletRequest request,
                                HttpServletResponse response){
        return adminService.doLogin(admin,request,response);
    }


    /**
     * 退出登录
     * 1.拿到tokenKey
     * 2.删除redis中的token
     * 3.删除mysql中的refreshtoken
     * 4.删除cookie中的tokenKey
     *
     *
     * @return
     */
    @GetMapping("/logout")
    public ResponseResult logout(HttpServletRequest request,
                                 HttpServletResponse response){
        return adminService.doLogout(request,response);

    }





}
