package com.example.demo2.utils;

import com.example.demo2.pojo.Admin;
import com.example.demo2.pojo.Jiaowu;
import com.example.demo2.pojo.Student;
import com.example.demo2.pojo.Teacher;
import io.jsonwebtoken.Claims;

import java.util.HashMap;
import java.util.Map;

public class ClaimsUtils {

    public static final String ADMINID = "adminId";
    public static final String STUDENTID = "studentId";
    public static final String JIAOWUID = "jiaowuId";
    public static final String TEACHERID = "teacherId";

    public static Map<String,Object> adminToclaims(Admin admin){
        Map<String,Object> claims = new HashMap<>();
        //这里只存放了管理员的ID，并没有放密码进去
        claims.put(ADMINID,admin.getAdminId());
        return claims;
    }

    public static Admin claimsToadmin(Claims claims){
        Admin admin = new Admin();
        String adminId = (String) claims.get(ADMINID);
        admin.setAdminId(adminId);
        return admin;
    }

    public static Map<String, Object> jiaowuToclaims(Jiaowu jiaowuById) {
        Map<String,Object> claims = new HashMap<>();
        claims.put(JIAOWUID,jiaowuById.getJiaowuId());
        return claims;
    }

    public static Jiaowu claimsTojiaowu(Claims claims){
        Jiaowu jiaowu = new Jiaowu();
        String jiaowuId = (String) claims.get(JIAOWUID);
        jiaowu.setJiaowuId(jiaowuId);
        return jiaowu;
    }

    public static Map<String, Object> studentToclaims(Student studentById) {
        Map<String,Object> claims = new HashMap<>();
        claims.put(STUDENTID,studentById.getStudentId());
        return claims;
    }

    public static Student claimsTostudent(Claims claims){
        Student student = new Student();
        String studentId = (String) claims.get(STUDENTID);
        student.setStudentId(studentId);
        return student;
    }

    public static Map<String, Object> teacherToclaims(Teacher teacherById) {
        Map<String,Object> claims = new HashMap<>();
        claims.put(TEACHERID,teacherById.getTeacherId());
        return claims;
    }

    public static Teacher claimsToteacher(Claims claims){
        Teacher teaher = new Teacher();
        String teazherId = (String) claims.get(TEACHERID);
        teaher.setTeacherId(teazherId);
        return teaher;
    }
}
