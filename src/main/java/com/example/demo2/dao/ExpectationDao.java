package com.example.demo2.dao;

import com.example.demo2.pojo.Expectation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface ExpectationDao extends JpaRepository<Expectation,String>, JpaSpecificationExecutor<Expectation> {


    /**
     * 分页列出学生志愿
     *
     * @param trainingId
     * @param pageable
     * @return
     */
    @Query(value = "SELECT student.student_id studentId,student_name studentName,student_class studentClass ," +
            "expectation_1 e1,expectation_2 e2,expectation_3 e3,expectation_4 e4,expectation_5 e5 " +
            "FROM student,expectation " +
            "WHERE student.student_id = expectation.student_id AND student.training_id = :trainingId",nativeQuery = true)
    Page<List<Map<String,Object>>> listAllE(int trainingId, Pageable pageable);


    /**
     * 查出单个学生的志愿
     *
     * @param studentId
     * @return
     */
    @Query(value = "SELECT student.student_id studentId,student_name studentName,student_class studentClass ," +
            "expectation_1 e1,expectation_2 e2,expectation_3 e3,expectation_4 e4,expectation_5 e5 " +
            "FROM student,expectation " +
            "WHERE student.student_id = expectation.student_id AND student.student_id = :studentId",nativeQuery = true)
    Map<String,Object> listOneE(String studentId);


    /**
     * 列出当前实训所有需要分配的志愿
     *
     * @param trainingId
     * @return
     */
    @Query(value = "SELECT student.student_id studentId,expectation_1 e1,expectation_2 e2,expectation_3 e3,expectation_4 e4,expectation_5 e5 " +
            "FROM student,expectation " +
            "WHERE student.student_id = expectation.student_id AND student.training_id = :trainingId AND " +
                "student.student_id NOT IN (SELECT student_id FROM student_project_grade)",nativeQuery = true)
    List<Map<String,Object>> listEByTraining(int trainingId);




}
