package com.example.demo2.dao;

import com.example.demo2.pojo.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface StudentDao extends JpaRepository<Student,String>, JpaSpecificationExecutor<Student> {

    /**
     * 查询某个教务下所有实训中的学生
     *
     * @param jiaowuId
     * @param pageable
     * @return
     */
    @Query(value = "SELECT student_id studentId,student_name studentName,student_class studentClass ,student_telephone studentTelephone ," +
            "training.training_id  trainingId,training_name trainingName " +
            "FROM student, training " +
            "WHERE student.training_id = training.training_id and training.jiaowu_id = :jiaowuId",nativeQuery = true)
    Page<List<Map<String,Object>>> listallWithoutpassword(String jiaowuId,Pageable pageable);

    @Query(value = "SELECT COUNT(student_id) FROM student",nativeQuery = true)
    int getStudentNum();

    /**
     * 查询某个实训下的所有学生
     *
     * @param trainingId
     * @param pageable
     * @return
     */
    @Query(value = "SELECT student_id studentId,student_name studentName,student_class studentClass ,student_telephone studentTelephone ," +
            "training.training_id  trainingId,training_name trainingName " +
            "FROM student, training " +
            "WHERE student.training_id = :trainingId",nativeQuery = true)
    Page<List<Map<String,Object>>> listAllStudentBytrainingId(Integer trainingId ,Pageable pageable);

    /**
     * 查询某个项目下的所有学生信息
     *
     * @param projectId
     * @param pageable
     * @return
     */
    @Query(value = "SELECT student.student_id studentId,student_name studentName,student_class studentClass ,student_telephone studentTelephone, " +
            "isleader isleader,isjointed isjointed ,grades_100points grade100,grades_5points grade5 " +
            "FROM student,student_project_grade " +
            "WHERE student.student_id = student_project_grade.student_id AND student_project_grade.project_id = :projectId",nativeQuery = true)
    Page<List<Map<String,Object>>> listAllStudentByProjectId(int projectId,Pageable pageable);


    /**
     * 列出某老师下的所有学生信息
     *
     * @param teacherId
     * @return
     */
    @Query(value = "SELECT student.student_id studentId,student_name studentName,student_class studentClass ,student_telephone studentTelephone," +
            "project.project_id projectId,project.project_name projectName,isleader isleader,grades_100points grade100 ,grades_5points grade5 " +
            "FROM student_project_grade,student,project " +
            "WHERE student.student_id = student_project_grade.student_id AND student_project_grade.project_id = project.project_id AND project.teacher_id =:teacherId",nativeQuery = true)
    List<Map<String,Object>> listAllStudentByTeacherId(String teacherId);
    /**
     * 根据ID查询学生的全部信息
     *
     * @param studentId
     * @return
     */
    Student findStudentByStudentId(String studentId);

    /**
     * 查询单个学生，不含密码
     *
     * @param studentId
     * @return
     */
    @Query(value = "SELECT student_id studentId,student_name studentName,student_class studentClass,student_telephone studentTelephone ,training.training_id trainingId,training_name trainingName " +
            "FROM student,training " +
            "WHERE student.training_id = training.training_id AND student_id = :studentId",nativeQuery = true)
    Map<String,Object> findOneByIdWithoutPassword(String studentId);


    /**
     * 查询出没有填报志愿并且没报创新实训的学生信息
     *
     * @param pageable
     * @param trainingId
     * @return
     */
    @Query(value = "SELECT student.student_id studentId,student_name studentName,student_class studentClass,student_telephone studentTelephone ,training.training_id trainingId,training_name trainingName " +
            "FROM student,training " +
            "WHERE student.training_id = training.training_id AND student.training_id = :trainingId " +
            "AND student_id NOT IN (SELECT student_id FROM expectation ) " +
            "AND student_id NOT IN (SELECT student_id FROM student_project_grade)",nativeQuery = true)
    Page<List<Map<String,Object>>> ListNoEStudent(int trainingId,Pageable pageable);

    /**
     * 根据学号删除学生
     *
     * @param studentId
     * @return
     */
    int deleteAllByStudentId(String studentId);

}



