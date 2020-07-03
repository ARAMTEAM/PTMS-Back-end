package com.example.demo2.response;

/*
统一返回接口
 */
public class ResponseResult {
    private boolean success;
    private int code;
    private String message;
    private Object data;

    //构造方法，方便书写代码
    public ResponseResult(ResponseState responseState) {
        this.success = responseState.isSuccess();
        this.code = responseState.getCode();
        this.message = responseState.getMessage();
    }

    public static ResponseResult SUCCESS(){
        return new ResponseResult(ResponseState.SUCCESS);
    }

    //重载方法,可以赋值一个新的message
    public static ResponseResult SUCCESS(String message){
        ResponseResult responseResult = new ResponseResult(ResponseState.SUCCESS);
        responseResult.setMessage(message);
        return responseResult;
    }



    public static ResponseResult FAILED(String message){
        ResponseResult responseResult = new ResponseResult(ResponseState.FAILED);
        responseResult.setMessage(message);
        return responseResult;
    }

    public static ResponseResult FAILED(){
        return new ResponseResult(ResponseState.FAILED);
    }

    public static ResponseResult PERMISSION_DENIED(){
        return new ResponseResult(ResponseState.PERMISSION_DENIED);
    }
    public static ResponseResult ERR_403(){
        return new ResponseResult(ResponseState.ERR_403);
    }

    public static ResponseResult ERR_404(){
        return new ResponseResult(ResponseState.ERR_404);
    }

    public static ResponseResult ERR_504(){
        return new ResponseResult(ResponseState.ERR_504);
    }

    public static ResponseResult ERR_505(){
        return new ResponseResult(ResponseState.ERR_505);
    }


    public static ResponseResult ACCOUNT_NOT_LOGIN(){
        return new ResponseResult(ResponseState.ACCOUNT_NOT_LOGIN);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

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

    public Object getData() {
        return data;
    }

    public ResponseResult setData(Object data) {
        this.data = data;
        return this;
    }
}
