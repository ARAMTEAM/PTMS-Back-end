package com.example.demo2;

import com.example.demo2.dao.ExpectationDao;
import com.example.demo2.pojo.Expectation;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class TestNumNull {
    public static void main(String[] args) {
//        Map<String,Object> mao= new HashMap<String,Object>();
//        mao.put("a",12);
//        if((Integer)mao.get("a") == 12){
//            System.out.println("true");
//        }

        long a = 123;
        int b =123;
        if(a == b ){
            System.out.println("1");
        }


    }
}
