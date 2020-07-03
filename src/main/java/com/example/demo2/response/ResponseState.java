package com.example.demo2.response;

import javax.xml.ws.Response;

public enum  ResponseState {
    //枚举
    SUCCESS(true,20000,"操作成功！"),
    LOGIN_SUCCESS(true,20001,"登录成功！"),

    FAILED(false,40000,"操作失败！"),
    ACCOUNT_NOT_LOGIN(false,40002,"账号未登录!"),
    GET_RESOURCES_FAILED(false,40000,"操作失败！"),
    PERMISSION_DENIED(false,40001,"权限不足!"),
    ERR_404(false,40004,"页面丢失!!"),
    ERR_403(false,40003,"权限不足!"),
    ERR_504(false,40005,"系统繁忙,请稍后重试!"),
    ERR_505(false,40006,"请求错误!请检查参数!!"),
    LOGIN_FAILED(true,49999,"登录失败！");

    ResponseState(boolean success,int code,String message){
        this.code = code;
        this.success = success;
        this.message = message;
    }

    private int code;
    private String message;
    private boolean success;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
