package com.example.demo2.dao;

import com.example.demo2.pojo.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.transaction.Transactional;

public interface RefreshTokenDao extends JpaRepository<RefreshToken,Integer>, JpaSpecificationExecutor<RefreshToken> {

    public RefreshToken findOneByUserIdAndUserType(String userId,String userType);

    public RefreshToken findOneByTokenKey(String tokenKey);

    int deleteAllByUserIdAndUserType(String userId,String userType);

    int deleteAllByTokenKey(String tokenKey);
}
