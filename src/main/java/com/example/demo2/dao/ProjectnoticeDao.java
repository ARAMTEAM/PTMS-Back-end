package com.example.demo2.dao;

import com.example.demo2.pojo.Projectnotice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface ProjectnoticeDao extends JpaRepository<Projectnotice,Integer>, JpaSpecificationExecutor<Projectnotice> {


    /**
     * 查询出某个项目的所有公告
     *
     * @param projectId
     * @param pageable
     * @return
     */
    @Query(value = "SELECT projectnotice_id projectnoticeId,projectnotice_title projectnoticeTitle,projectnotice_content projectnoticeContent," +
            "projectnotice_create_time projectnoticeCreateTime,projectnotice_update_time projectnoticeUpdateTime," +
            "project_name projectName,teacher_name teacherName " +
            "FROM projectnotice,project,teacher " +
            "WHERE projectnotice.project_id = project.project_id AND project.teacher_id = teacher.teacher_id AND project.project_id =:projectId",nativeQuery = true)
    Page<List<Map<String,Object>>> listAllnoticeByProjectId(Integer projectId, Pageable pageable);


    /**
     * 查询出某个项目的所有公告
     *
     * @param projectId
     * @return
     */
    @Query(value = "SELECT projectnotice_id projectnoticeId,projectnotice_title projectnoticeTitle,projectnotice_content projectnoticeContent," +
            "projectnotice_create_time projectnoticeCreateTime,projectnotice_update_time projectnoticeUpdateTime," +
            "project_name projectName,teacher_name teacherName " +
            "FROM projectnotice,project,teacher " +
            "WHERE projectnotice.project_id = project.project_id AND project.teacher_id = teacher.teacher_id AND project.project_id =:projectId",nativeQuery = true)
    List<Map<String,Object>> listAllnoticeByProjectIdNopage(Integer projectId);


    /**
     * 根据ID查出单个公告
     *
     * @param projectnoticeId
     * @return
     */
    Projectnotice findByProjectnoticeId(int projectnoticeId);
}
