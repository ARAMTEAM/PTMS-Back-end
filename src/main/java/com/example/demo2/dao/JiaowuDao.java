package com.example.demo2.dao;

import com.example.demo2.pojo.Jiaowu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface JiaowuDao extends JpaRepository<Jiaowu,String>, JpaSpecificationExecutor<Jiaowu> {


    /**
     * 根据教务id查找
     *
     * @param jiaowuId
     * @return
     */
    Jiaowu findOneByJiaowuId(String jiaowuId);

    /**
     * 查询教务的部分属性并分页输出
     *
     * @param pageable
     * @return
     */
    @Query(value = "SELECT jiaowu_id  jiaowuId,jiaowu_name  jiaowuName,jiaowu_dept  jiaowuDept,jiaowu_nianji jiaowuNianji,jiaowu_username jiaowuUsername FROM jiaowu",nativeQuery = true)
    public Page<List<Map<String,Object>>> findByPage(Pageable pageable);


    /**
     * 查询全部教务的部分属性
     *
     * @param pageable
     * @return
     */
    @Query(value = "SELECT new Jiaowu(j.jiaowuId,j.jiaowuDept,j.jiaowuName,j.jiaowuUsername,j.jiaowuNianji) FROM Jiaowu as j")
    Page<Jiaowu> listAllJiaowuNoPassword(Pageable pageable);


    @Query(value = "SELECT new Jiaowu(j.jiaowuId,j.jiaowuDept,j.jiaowuName,j.jiaowuUsername,j.jiaowuNianji) FROM Jiaowu as j WHERE j.jiaowuId=:jiaowuId")
    Jiaowu listOneJiaowu(String jiaowuId);
}
