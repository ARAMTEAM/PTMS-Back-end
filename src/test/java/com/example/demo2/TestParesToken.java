package com.example.demo2;

import com.example.demo2.utils.JwtUtil;
import io.jsonwebtoken.Claims;

public class TestParesToken {
    public static void main(String[] args) {
        Claims claims = JwtUtil.parseJWT("eyJhbGciOiJIUzI1NiJ9.eyJhZG1pbklkIjoic2R1MDAxIiwiZXhwIjoxNTkyODI2NzU4LCJhZG1pblBhc3N3b3JkIjoiMTIzNDU2In0.waB-5FBFshkx3mr9Q6N743gTEPrrtWzEy6Mx4kMG0R0");
        //==============================================//
        Object adminId = claims.get("adminId");
        Object adminPassword = claims.get("adminPassword");

        System.out.println("adminId == > " + adminId);
        System.out.println("adminPassword == > " + adminPassword);
    }
}