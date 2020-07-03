package com.example.demo2;

import com.example.demo2.utils.IPUtil;
import com.example.demo2.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j  //log插件
@SpringBootApplication
public class Demo2Application {

    public static void main(String[] args) {
        log.info("Demo2Application run************************");
        SpringApplication.run(Demo2Application.class, args);
    }

    @Bean
    public BCryptPasswordEncoder createPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RedisUtils createRedisUtils(){
        return new RedisUtils();
    }

    @Bean
    public IPUtil createIPUtil(){
        return new IPUtil();
    }
}
