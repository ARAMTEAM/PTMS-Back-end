package com.example.demo2.controller.jiaowu;

import com.example.demo2.pojo.Jiaowu;
import com.example.demo2.response.ResponseResult;
import com.example.demo2.services.JiaowuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/jiaowu")
public class JiaowuApi {

    @Autowired
    private JiaowuService jiaowuService;

    /**
     * 增加一个教务
     *
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 1")
    @PostMapping
    public ResponseResult createNewJiaowu(@RequestBody Jiaowu jiaowu,HttpServletRequest request,HttpServletResponse response){
        return jiaowuService.createNewJiaowu(jiaowu,request,response);
    }


    /**
     * 删除一个教务
     *
     * @param jiaowuId
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 1")
    @DeleteMapping("/{jiaowuId}")
    public ResponseResult deletejiaowu(@PathVariable("jiaowuId") String jiaowuId,HttpServletRequest request,HttpServletResponse response){
        log.info("收到了一条删除请求！"+ jiaowuId);
        return jiaowuService.deleteOneJiaowu(jiaowuId,request,response);
    }


    /**
     * 教务登录
     *
     * @param jiaowu
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/login")
    public ResponseResult jiaowulogin(@RequestBody Jiaowu jiaowu,
                                      HttpServletRequest request,
                                      HttpServletResponse response){
        return jiaowuService.JiaowuLogin(jiaowu,request,response);
    }

    /**
     * 退出登录
     *
     * @return
     */
    @GetMapping("/logout")
    public ResponseResult logout(){
        return jiaowuService.jiaowuLogout();
    }

    /**
     * 修改教务信息
     *
     * @param jiaowu
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 1")
    @PutMapping
    public ResponseResult updatejiaowu(@RequestBody Jiaowu jiaowu,HttpServletRequest request,HttpServletResponse response){
        return jiaowuService.updateOneJiaowu(jiaowu,request,response);
    }


    /**
     * 分页列出教务
     *
     * @param page
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 1")
    @GetMapping("/{page}")
    public ResponseResult listJiaowu(@PathVariable("page") int page,
                                     HttpServletRequest request,
                                     HttpServletResponse response){
        return jiaowuService.listJiaowu(page,request,response);
    }

    /**
     * 获取单个教务的信息
     *
     * @param jiaowuId
     * @return
     */
    @PreAuthorize("@permission.CheckUser() >= 1")
    @GetMapping("/one/{jiaowuId}")
    public ResponseResult listOneJiaowu(@PathVariable("jiaowuId") String jiaowuId){
        return jiaowuService.listOneJiaowu(jiaowuId);
    }


}
