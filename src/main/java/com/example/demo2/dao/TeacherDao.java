package com.example.demo2.dao;

import com.example.demo2.pojo.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface TeacherDao extends JpaRepository<Teacher,String>, JpaSpecificationExecutor<Teacher> {

    /**
     * 分页查询出某教务下所有实训的老师
     *
     * @param jiaowuId
     * @return
     */
    @Query(value = "SELECT teacher_id teacherId,teacher_name teacherName,teacher_dept teacherDept ,teacher_rank teacherRank ,teacher_telephone teacherTelephone ,teacher_email teacherEmail ," +
            "teacher.training_id  trainingId,training_name trainingName " +
            "FROM teacher,training  " +
            "WHERE training.jiaowu_id = :jiaowuId AND teacher.training_id = training.training_id",nativeQuery = true)
    Page<List<Map<String,Object>>> findAllteacherWithoutPasswordByjiaowuid(String jiaowuId, Pageable pageable);

    /**
     * 不分页查询出某教务下所有实训的老师
     *
     * @param jiaowuId
     * @return
     */
    @Query(value = "SELECT teacher_id teacherId,teacher_name teacherName,teacher_dept teacherDept ,teacher_rank teacherRank ,teacher_telephone teacherTelephone ,teacher_email teacherEmail ," +
            "training_id  trainingId,training_name trainingName " +
            "FROM teacher t1 NATURAL JOIN training t2 " +
            "WHERE t2.jiaowu_id = :jiaowuId",nativeQuery = true)
    List<Map<String,Object>> listAllTeacherByJiaowuNoPage(String jiaowuId );

    @Query(value = "SELECT COUNT(teacher_id) FROM teacher",nativeQuery = true)
    int getTeacherNum();

    /**
     * 列出可以选择的指导教师
     *
     * @param trainingId
     * @return
     */
    @Query(value = "SELECT teacher_id teacherId,teacher_name teacherName,teacher_dept teacherDept ,teacher_rank teacherRank ,teacher_telephone teacherTelephone ,teacher_email teacherEmail "+
            "FROM teacher  " +
            "WHERE training_id = :trainingId",nativeQuery = true)
    List<Map<String,Object>> listAllTeacherCanChoose(int trainingId );



    /**
     * 根据教师ID查找教师（有密码）
     *
     * @param teacherId
     * @return
     */
    Teacher findTeacherByTeacherId(String teacherId);

    /**
     * 根据教师ID查找教师（无密码）
     *
     * @param teacherId
     * @return
     */
    @Query(value = "SELECT teacher_id teacherId,teacher_name teacherName,teacher_dept teacherDept ,teacher_rank teacherRank ,teacher_telephone teacherTelephone ,teacher_email teacherEmail,training_id trainingId " +
            "FROM teacher " +
            "WHERE teacher_id = :teacherId",nativeQuery = true)
    Map<String,Object> findByIdWithoutPassword(String teacherId);



    /**
     * 根据教师号删除教师
     *
     * @param teacherId
     * @return
     */
    int deleteTeacherByTeacherId(String teacherId);


}
