package com.example.demo2;

import com.example.demo2.utils.Constants;
import com.example.demo2.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class TestRedis {
//


    public static void main(String[] args) {
        RedisUtils redisUtils = new RedisUtils();
        redisUtils.set(Constants.User.KEY_CONTENT + "123456","resq",60 * 2);
        System.out.println(redisUtils.get(Constants.User.KEY_CONTENT + "123456"));
        System.out.println("success");
    }

}
