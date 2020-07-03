package com.example.demo2.dao;

import com.example.demo2.pojo.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface ProjectDao extends JpaRepository<Project,Integer>, JpaSpecificationExecutor<Project> {


    /**
     * 根据实训ID，查询出所有的项目信息
     *
     * @param trainingId
     * @param pageable
     * @return
     */
    @Query(value = "SELECT project_id projectId,project_name projectName,project_status projectStatus,project_max_num projectMaxNum,project_introduction projectIntroduction," +
            "project_helper projectHelper,project_helper_tel projectHelperTel,project_interval_day projectIntervalDay,project_applicant_type projectApplicantType,project.training_id trainingId," +
            "project.teacher_id teacherId,teacher_name teacherName " +
            "FROM project,teacher WHERE project.training_id = :trainingId AND project.teacher_id = teacher.teacher_id",nativeQuery = true)
    Page<List<Map<String,Object>>> listAllProjectByTrainingId(Integer trainingId, Pageable pageable);

    /**
     * 根据实训ID，查询出所有的项目信息
     *
     * @param trainingId
     * @return
     */
    @Query(value = "SELECT project_id projectId,project_name projectName,project_status projectStatus,project_max_num projectMaxNum,project_introduction projectIntroduction," +
            "project_helper projectHelper,project_helper_tel projectHelperTel,project_interval_day projectIntervalDay,project_applicant_type projectApplicantType,project.training_id trainingId," +
            "project.teacher_id teacherId,teacher_name teacherName " +
            "FROM project,teacher WHERE project.training_id = :trainingId AND project.teacher_id = teacher.teacher_id",nativeQuery = true)
    List<Map<String,Object>> listAllProjectByTrainingIdNoPage(Integer trainingId);

    /**
     * 列出所有的教师项目
     *
     * @param trainingId
     * @param pageable
     * @return
     */
    @Query(value = "SELECT project_id projectId,project_name projectName,project_status projectStatus,project_max_num projectMaxNum,project_introduction projectIntroduction," +
            "project_helper projectHelper,project_helper_tel projectHelperTel,project_interval_day projectIntervalDay,project_applicant_type projectApplicantType,project.training_id trainingId," +
            "project.teacher_id teacherId, teacher_name teacherName " +
            "FROM project,teacher " +
            "WHERE  project.training_id = :trainingId AND project.teacher_id = teacher.teacher_id AND project_applicant_type = '教师'" ,nativeQuery = true)
    Page<List<Map<String,Object>>> listAllTeacherProjectByTrainingId(Integer trainingId, Pageable pageable);


    /**
     * 教务列出所有的待审核
     *
     * @param pageable
     * @return
     */
    @Query(value = "SELECT project_id projectId,project_name projectName,project_status projectStatus,project_max_num projectMaxNum,project_introduction projectIntroduction," +
            "project_helper projectHelper,project_helper_tel projectHelperTel,project_interval_day projectIntervalDay,project_applicant_type projectApplicantType,project.training_id trainingId," +
            "project.teacher_id teacherId ,training_name trainingName " +
            "FROM project,training " +
            "WHERE  project.training_id = training.training_id AND training.jiaowu_id = :jiaowuId AND project_status = '待审核' ",nativeQuery = true)
    Page<List<Map<String,Object>>> listAllNoAuditProject(String jiaowuId, Pageable pageable);

    /**
     * 教师列出以他为指导教师的创新项目
     *
     * @return
     */
    @Query(value = "SELECT project.project_id projectId,project_name projectName,project_status projectStatus,project_max_num projectMaxNum,project_introduction projectIntroduction," +
            "project_helper projectHelper,project_helper_tel projectHelperTel,project_interval_day projectIntervalDay,project_applicant_type projectApplicantType,project.training_id trainingId," +
            "project.teacher_id teacherId,student_name studentName " +
            "FROM project ,student_project_grade,student " +
            "WHERE  project.project_id = student_project_grade.project_id  AND student.student_id = student_project_grade.student_id AND student_project_grade.isleader = 1 " +
            "AND project.teacher_id = :teacherId " +
            "AND project.project_status in ('等待教师审核','教师未同意')",nativeQuery = true)
    List<Map<String,Object>> listAllTeacherNeedReceive(String teacherId  );



    /**
     * 列出所有的的需答辩
     *
     * @param pageable
     * @return
     */
    @Query(value = "SELECT project_id projectId,project_name projectName,project_status projectStatus,project_max_num projectMaxNum,project_introduction projectIntroduction," +
            "project_helper projectHelper,project_helper_tel projectHelperTel,project_interval_day projectIntervalDay,project_applicant_type projectApplicantType,project.training_id trainingId," +
            "project.teacher_id teacherId ,training_name trainingName " +
            "FROM project,training WHERE project.training_id = training.training_id AND training.jiaowu_id = :jiaowuId AND project_status = '需答辩' ",nativeQuery = true)
    Page<List<Map<String,Object>>> listAllNeedDebateProject(String jiaowuId, Pageable pageable);



    /**
     * 查出所有可以选择的项目
     *
     * @param trainingId
     * @return
     */
    @Query(value = "select new Project(p.projectId,p.projectMaxNum)from Project as p where p.trainingId=:trainingId and p.projectApplicantType='教师'")
    List<Project> listProjectCanChoose(int trainingId);


    /**
     * 分页列出某个教师的全部项目
     *
     * @param teacherId
     * @param pageable
     * @return
     */
    @Query(value = "SELECT project_id projectId,project_name projectName,project_status projectStatus,project_max_num projectMaxNum,project_introduction projectIntroduction," +
            "project_helper projectHelper,project_helper_tel projectHelperTel,project_interval_day projectIntervalDay,project_applicant_type projectApplicantType,project.training_id trainingId," +
            "project.teacher_id teacherId, teacher_name teacherName " +
            "FROM project,teacher WHERE project.teacher_id = teacher.teacher_id AND teacher.teacher_id = :teacherId ",nativeQuery = true)
    Page<List<Map<String,Object>>> listAllByTeacherId(String teacherId,Pageable pageable);

    /**
     * 根据项目ID返回信息
     *
     * @param projectID
     * @return
     */
    Project findByProjectId(int projectID);

    /**
     * 根据申请类型返回项目信息
     *
     * @param Applicant
     * @return
     */
    Project findByProjectApplicantType(String Applicant);


}
