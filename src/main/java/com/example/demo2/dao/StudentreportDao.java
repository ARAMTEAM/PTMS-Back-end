package com.example.demo2.dao;

import com.example.demo2.pojo.Studentreport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface StudentreportDao extends JpaRepository<Studentreport,Integer>, JpaSpecificationExecutor<Studentreport> {


    /**
     * 分页查出某个学生的所有报告
     *
     * @param studentId
     * @param pageable
     * @return
     */
    @Query(value = "SELECT studentreport_id studentreportId,studentreport_type studentreportType,studentreport_title studentreportTitle," +
            "studentreport_date studentreportDate,studentreport_filePath studentreportFilepath,studentreport_filename studentreportFilename,studentreport_status studentreportStatus," +
            "student.student_id studentId, student_name studentName " +
            "FROM studentreport,student " +
            "WHERE studentreport.student_id = student.student_id AND studentreport.student_id =:studentId",nativeQuery = true)
    Page<List<Map<String,Object>>> listAllReportByStudentId(String studentId, Pageable pageable);

    /**
     * 不分页查出某个学生的所有报告
     *
     * @param studentId
     * @return
     */
    @Query(value = "SELECT studentreport_id studentreportId,studentreport_type studentreportType,studentreport_title studentreportTitle," +
            "studentreport_date studentreportDate,studentreport_filePath studentreportFilepath,studentreport_status studentreportStatus," +
            "student.student_id studentId, student_name studentName " +
            "FROM studentreport,student " +
            "WHERE studentreport.student_id = student.student_id AND studentreport.student_id =:studentId",nativeQuery = true)
    List<Map<String,Object>> listAllReportByStudentIdNppage(String studentId);


    /**
     * 查询单个报告
     *
     * @param id
     * @return
     */
    Studentreport findByStudentreportId(int id);


}
