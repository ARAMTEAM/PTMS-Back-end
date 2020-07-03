package com.example.demo2.dao;

import com.example.demo2.pojo.Adminnotice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface AdminnoticeDao extends JpaRepository<Adminnotice,Integer>, JpaSpecificationExecutor<Adminnotice> {

    /**
     * 分页
     *
     * @param pageable
     * @return
     */
    @Query(value = "SELECT adminnotice_id adminnoticeId," +
            "adminnotice_title adminnoticeTitle," +
            "adminnotice_content adminnoticeContent," +
            "adminnotice_create_time adminnoticeCreateTime," +
            "adminnotice_update_time adminnoticeUpdateTime " +
            "From adminnotice",nativeQuery = true)
    public Page<List<Map<String,Object>>> findByPage(Pageable pageable);

    /**
     * 根据公告ID查询公告
     *
     * @param id
     * @return
     */
    public Adminnotice findOneByAdminnoticeId(Integer id);
}
