package com.example.demo2.dao;

import com.example.demo2.pojo.FileResource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileDao extends JpaRepository<FileResource,Integer> {
}
