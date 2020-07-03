package com.example.demo2.controller;

import com.example.demo2.response.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 错误码统一返回接口
 *
 */
@RestController
public class ErrorPageController {

    @GetMapping("/403")
    public ResponseResult page403(){
        return ResponseResult.ERR_403();
    }

    @GetMapping("/404")
    public ResponseResult page404(){
        return ResponseResult.ERR_404();
    }

    @GetMapping("/504")
    public ResponseResult page504(){
        return ResponseResult.ERR_504();
    }

    @GetMapping("/505")
    public ResponseResult page505(){
        return ResponseResult.ERR_505();
    }
}
