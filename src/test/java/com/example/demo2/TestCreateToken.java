package com.example.demo2;

import com.example.demo2.utils.JwtUtil;

import java.util.HashMap;
import java.util.Map;

public class TestCreateToken {
    public static void main(String[] args) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("adminId", "sdu001");
        claims.put("adminPassword","123456");
        String token = JwtUtil.createToken(
                claims);//有效期为1分钟
        System.out.println(token);
    }
}
