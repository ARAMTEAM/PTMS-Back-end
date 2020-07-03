package com.example.demo2.dao;

import com.example.demo2.pojo.Training;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface TrainingDao extends JpaRepository<Training,Integer>, JpaSpecificationExecutor<Training> {


    /**
     * 根据教务ID和页面查询实训，但不返回教务ID
     *
     * @param jiaowuId
     * @param pageable
     * @return
     */
    @Query(value = "select training_id trainingId,training_name trainingName,training_notice trainingNotice,training_time_1 trainingTime1,training_time_2 trainingTime2,training_time_3 trainingTime3,training_time_4 trainingTime4,training_time_5 trainingTime5,training_time_6 trainingTime6,kecheng_id kechengId,kexu_id kexuID " +
            "from training " +
            "where jiaowu_id = :jiaowuId",nativeQuery = true)
    Page<List<Map<String,Object>>> findByJiaowuId(String jiaowuId,Pageable pageable);

    /**
     * 根据教务ID和页面查询实训，但不返回教务ID
     *
     * @param jiaowuId
     * @return
     */
    @Query(value = "select training_id trainingId,training_name trainingName,training_notice trainingNotice,training_time_1 trainingTime1,training_time_2 trainingTime2,training_time_3 trainingTime3,training_time_4 trainingTime4,training_time_5 trainingTime5,training_time_6 trainingTime6,kecheng_id kechengId,kexu_id kexuID " +
            "from training " +
            "where jiaowu_id = :jiaowuId",nativeQuery = true)
    List<Map<String,Object>> findByJiaowuIdNoPage(String jiaowuId);


//    下面的方法虽然也可以做到，但是会返回一个空的教务ID字符串
//    @Query(value = "SELECT new Training (t.trainingId,t.trainingName,t.trainingNotice,t.trainingTime1,t.trainingTime2,t.trainingTime3,t.trainingTime4,t.trainingTime5,t.trainingTime6,t.kechengId,t.kexuID) " +
//            "FROM Training as t " +
//            "where t.jiaowuId = :jiaowuId")
//    Page<Training> listAllTrainingByJiaowuId(String jiaowuId, Pageable pageable);
//
//    Page<Training> findAllByJiaowuId(String jiaowuId,Pageable pageable);

    /**
     * 根据实训ID返回这个实训的相关信息
     *
     * @param trainingId
     * @return
     */
    Training findByTrainingId(Integer trainingId);

    /**
     * 根据实训ID返回这个实训的相关信息，包括教务老师的名字
     *
     * @param trainingId
     * @return
     */
    @Query(value = "select training_id trainingId,training_name trainingName,jiaowu_name jiaowuName, training_notice notice,training_time_1 time1,training_time_2 time2,training_time_3 time3,training_time_4 time4,training_time_5 time5,training_time_6 time6,kecheng_id kechengId,kexu_id kexuId " +
            "from training natural join jiaowu " +
            "where training_id = :trainingId",nativeQuery = true)
    List<Map<String,Object>> findjiaowuname(Integer trainingId);

    /**
     * 根据实训ID删除实训
     *
     * @param trainingId
     * @return
     */
    int deleteTrainingByTrainingId(Integer trainingId);


}
