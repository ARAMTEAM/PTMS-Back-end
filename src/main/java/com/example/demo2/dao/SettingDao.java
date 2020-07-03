package com.example.demo2.dao;

import com.example.demo2.pojo.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SettingDao extends JpaRepository<Setting,String>, JpaSpecificationExecutor<Setting> {
    Setting findOneByAkey(String akey);
}
