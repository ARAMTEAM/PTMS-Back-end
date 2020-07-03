package com.example.demo2.dao;

import com.example.demo2.pojo.Slogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SlogsDao extends JpaRepository<Slogs,Integer>, JpaSpecificationExecutor<Slogs> {
}
