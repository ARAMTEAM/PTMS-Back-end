package com.example.demo2;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestPasswordEncoder {
    public static void main(String[]args){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode("123456");  //每一次都不一样

        //$2a$10$MS3P1qwYhIWHs/DwQAjnvuBNkYq28DzRYzZIIdAxiST2vwRTLzIzW
        System.out.println("encode == > "+encode +"   length "+encode.length());

        //验证登录流程
        //1.用户提交密码  123456
        //2.跟数据库中的密文比较，判断是否匹配
        String orginalPassword = "000000";
        boolean matches = passwordEncoder.matches(orginalPassword,"$2a$10$HwSENQmZSRwKjvTWmLIgoO/wLuS9dhM7tOX2hD3GT71ykdIEXcSo.");
        System.out.println("密码是否正确 === > " +matches);
    }
}
