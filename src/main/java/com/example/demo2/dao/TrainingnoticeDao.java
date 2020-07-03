package com.example.demo2.dao;

import com.example.demo2.pojo.Trainingnotice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface TrainingnoticeDao extends JpaRepository<Trainingnotice,Integer>, JpaSpecificationExecutor<Trainingnotice> {

    /**
     * 根据实训ID列出所有的公告
     *
     * @param trainingId
     * @param pageable
     * @return
     */
    @Query(value = "SELECT trainingnotice_id trainingnoticeId, trainingnotice_title trainingnoticeTitle,trainingnotice_content trainingnoticeContent," +
            "training.training_id trainingId,trainingnotice_create_time trainingnoticeCreateTime," +
            "trainingnotice_update_time trainingnoticeUpdateTime, " +
            "training_name trainingName,jiaowu_name jiaowuName " +
            "FROM jiaowu,trainingnotice,training " +
            "WHERE training.training_id = :trainingId AND training.jiaowu_id = jiaowu.jiaowu_id",nativeQuery = true)
    Page<List<Map<String,Object>>> listByTrainingId(String trainingId, Pageable pageable);


}
