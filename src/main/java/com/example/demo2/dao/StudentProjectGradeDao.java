package com.example.demo2.dao;

import com.example.demo2.pojo.StudentProjectGrade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface StudentProjectGradeDao extends JpaRepository<StudentProjectGrade,String>, JpaSpecificationExecutor<StudentProjectGrade> {


    /**
     * 根据学生ID查找简单信息
     *
     * @param studentId
     * @return
     */
    StudentProjectGrade findByStudentId(String studentId);

    /**
     * 根据学生ID查找包含项目名称
     *
     * @param studentId
     * @return
     */
    @Query(value = "SELECT student_id studentId,project.project_id projectId, project_name projectName," +
            "isleader isleader,isjointed isjointed,grades_100points grades100points,grades_5points grade5points," +
            "project.training_id trainingId," +
            "project_status projectStatus,project_max_num projectMaxNum,project_introduction projectIntroduction," +
            "project_helper projectHelper,project_helper_tel projectHelperTel,project_interval_day projectIntervalDay,project_applicant_type projectApplicantType," +
            "project.teacher_id teacherId,teacher_name  " +
            "FROM student_project_grade,project,teacher " +
            "WHERE student_project_grade.project_id = project.project_id AND project.teacher_id = teacher.teacher_id AND student_id = :studentId",nativeQuery = true)
    Map<String,Object> findProjectMessageStudentId(String studentId);

    /**
     * 查找某个项目的报名人数
     *
     * @param projectId
     * @return
     */
    @Query(value = "SELECT COUNT(student_id) SumNum FROM student_project_grade WHERE project_id = :projectId",nativeQuery = true)
    Map<String,Object> SumNumOfProject(int projectId);

    /**
     * 根据项目ID查找其中的学生信息
     *
     * @param projectId
     * @return
     */
    @Query(value = "SELECT project_id projectId,student.student_id studentId,student_name studentName,student_class studentClass,student_telephone studentTelephone,isleader isleader " +
            "FROM student , student_project_grade " +
            "WHERE student.student_id = student_project_grade.student_id AND project_id = :projectId",nativeQuery = true)
    List<Map<String,Object>> listAllItemByProjectId(Integer projectId);

    /**
     * 导出成绩
     *
     * @param trainingId
     * @return
     */
    @Query(value = "SELECT student.student_id studentId,student_name studentName,grades_100points grade100,grades_5points grade5 " +
            "FROM student,student_project_grade " +
            "WHERE student.student_id = student_project_grade.student_id AND student.training_id = :trainingId",nativeQuery = true)
    List<Map<String,Object>> GradeOutPut(int trainingId);

    /**
     * 查找出所有不在项目中的学生
     *
     * @param trainingId
     * @param pageable
     * @return
     */
    @Query(value = "SELECT student_id studentId,student_name studentName,student_class studentClass,student_telephone studentTelephone " +
            "FROM student WHERE student_id IN (SELECT student_id FROM expectation) AND student_id NOT IN (SELECT student_id FROM student_project_grade)",nativeQuery = true)
    Page<List<Map<String,Object>>> listNoProjectStudent(int trainingId, Pageable pageable);

    /**
     * 更新百分制成绩
     *
     * @param studentId
     * @param grade
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE student_project_grade SET grades_100points = :grade WHERE student_id = :studentId",nativeQuery = true)
    void TeacherUpdate100Grade(String studentId, int grade);

    /**
     * 更新5分制成绩
     *
     * @param studentId
     * @param grade
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE student_project_grade SET grades_5points = :grade WHERE student_id = :studentId",nativeQuery = true)
    void TeacherUpdate5Grade(String studentId, double grade);

}
