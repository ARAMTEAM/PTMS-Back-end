package com.example.demo2;

import org.springframework.util.DigestUtils;

public class TestCreateJwtMd5Value {
    public static void main(String[]args){
        //f1969c6e7552e8975d94cb8a72dbcc49
        String jwtKeyMd5Str = DigestUtils.md5DigestAsHex("PTMS_ARAM_-=".getBytes());
        System.out.println(jwtKeyMd5Str);
    }
}
