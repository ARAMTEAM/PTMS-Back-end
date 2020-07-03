package com.example.demo2.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtil {

    //盐值,不能给客户端
    /**
     * 1.算出token的md5值，把这个md5值给客户端，就是key，将这个token保存到redis
     * 2.解析过程：用户访问的时候，携带md5 KEY，从redis从拿出token，解析token，就知道用户是否有效
     * 引发的问题：假设有一百万个用户都登录过了，就有一百万条token在redis中，浪费资源
     * 所以要设置token的有效期--＞比如两个小时，过期后redis会自动删除,2
     * 但2小时后用户要再次登录,就会很麻烦
     * 可以通过一个refreshToken(一个月有效期,保存在数据库中)来生成新的token(2小时有效期,保存在redis)
     * double token 的解决方案
     *
     *
     */

    private static String key = "f1969c6e7552e8975d94cb8a72dbcc49";
    //单位是毫秒
    private static long ttl = Constants.TimeValue.HOUR_2 * 1000;//2个小时

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    /**
     * @param claims 载荷内容
     * @param ttl    有效时长
     * @return
     */
    public static String createToken(Map<String, Object> claims, long ttl) {  //createJWT
        JwtUtil.ttl = ttl;
        return createToken(claims);
    }


    public static String createRefreshToken(String userId, long ttl) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder().setId(userId)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, key);
        if (ttl > 0) {
            builder.setExpiration(new Date(nowMillis + ttl));
        }
        return builder.compact();
    }

    /**
     * @param claims 载荷
     * @return token
     */
    public static String createToken(Map<String, Object> claims) {

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, key);

        if (claims != null) {
            builder.setClaims(claims);
        }

        if (ttl > 0) {
            builder.setExpiration(new Date(nowMillis + ttl));
        }
        return builder.compact();
    }

    public static Claims parseJWT(String jwtStr) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwtStr)
                .getBody();
    }

}