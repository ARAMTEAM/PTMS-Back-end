package com.example.demo2.controller.adminnotice;

import com.example.demo2.pojo.Adminnotice;
import com.example.demo2.response.ResponseResult;
import com.example.demo2.services.AdminnoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/adminnotice")
public class AdminnoticeApi {

    @Autowired
    private AdminnoticeService adminnoticeService;

    /**
     * 新增一条管理员公告
     *
     * @param adminnotice
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 1")
    @PostMapping("")
    public ResponseResult addAdminNotice(@RequestBody Adminnotice adminnotice ,
                                         HttpServletRequest request ,
                                         HttpServletResponse response){
         return adminnoticeService.addAdminnotice(adminnotice,request,response);
    }


    /**
     * 分页查询公告
     *
     * @param page
     * @return
     */
    @PreAuthorize("@permission.CheckUser() >= 1")
    @GetMapping("/{page}")
    public ResponseResult showAdminNotice(@PathVariable("page") int page){
        return adminnoticeService.findAllAdminnotice(page);
    }


    /**
     * 更新一条公告
     *
     * @param adminnotice
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 1")
    @PutMapping
    public ResponseResult updateAdminNotice(@RequestBody Adminnotice adminnotice ,
                                            HttpServletRequest request ,
                                            HttpServletResponse response){
        return adminnoticeService.updateAdminnotice(adminnotice,request,response);
    }

    /**
     * 删除一条公告
     *
     * @param adminnoticeId
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 1")
    @DeleteMapping("/{adminnoticeId}")
    public ResponseResult deleteAdminNotice(@PathVariable("adminnoticeId") int adminnoticeId,
                                            HttpServletRequest request ,
                                            HttpServletResponse response){
        return adminnoticeService.deleteAdminnotice(adminnoticeId,request,response);
    }


}
