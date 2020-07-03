package com.example.demo2.dao;

import com.example.demo2.pojo.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AdminDao extends JpaRepository<Admin, String>, JpaSpecificationExecutor<Admin> {

    /**
     * 根据管理员id查找
     *
     * @param adminId
     * @return
     */
    Admin findOneByAdminId(String adminId);

}
