package com.example.demo2.controller.studentreport;

import com.example.demo2.response.ResponseResult;
import com.example.demo2.services.StudentreportService;
import com.example.demo2.services.impl.StudentreportServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/studentreport")
public class StudentreportApi {

    @Autowired
    private StudentreportService studentreportService;
    @Autowired
    private StudentreportServiceImpl studentreportServiceimpl;

    /**
     * 教师查看学生的所有报告
     *
     * @param studentId
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 4")
    @GetMapping("/teachersee/{studentId}/{page}")
    public ResponseResult listAllReportByStudentIdToTeacher(@PathVariable("studentId") String studentId,
                                                            @PathVariable("page") int page,
                                                            HttpServletRequest request,
                                                            HttpServletResponse response){
        return studentreportService.listAllReportByStudentIdToTeacher(studentId,page,request,response);
    }

    /**
     * 学生查看自己的所有报告
     *
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 3")
    @GetMapping("/studentsee/{page}")
    public ResponseResult listAllReportByStudentIdToStudent( @PathVariable("page") int page,
                                                            HttpServletRequest request,
                                                            HttpServletResponse response){
        return studentreportService.listAllReportByStudentIdToStudent(page,request,response);
    }

    /**
     * 学生上传报告
     *
     * @param title
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 3")
    @PostMapping("/{title}/{type}")
    @ResponseBody
    public ResponseResult studentAddReport(@PathVariable("title")String title,
                                           @PathVariable("type")int type,
                                           @RequestParam("file") MultipartFile file,
                                           HttpServletRequest request,
                                           HttpServletResponse response){
        return studentreportService.studentAdd(title,type,file,request,response);
    }


    /**
     * 下载报告
     *
     * @param fileId
     * @param res
     * @param request
     * @return
     * @throws IOException
     */
    @PreAuthorize("@permission.CheckUser() >= 3")
    @GetMapping("/download")
    @ResponseBody
    public String downloadFile(@RequestParam("fileId") int fileId , HttpServletResponse res,HttpServletRequest request) throws IOException {
        return studentreportService.downloadNoPer(request,res,fileId);
    }

    /**
     * 教师阅读
     *
     * @param studentreportId
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 4")
    @PostMapping("/read/{studentreportId}")
    public ResponseResult teacherRead(@PathVariable("studentreportId")int studentreportId,
                                      HttpServletRequest request,
                                      HttpServletResponse response){
        return studentreportService.teacherRead(studentreportId,request,response);
    }

    /**
     * 学生删除
     *
     * @param studentreportId
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 3")
    @DeleteMapping("/{studentreportId}")
    public ResponseResult responseResult(@PathVariable("studentreportId")int studentreportId,
                                         HttpServletRequest request,
                                         HttpServletResponse response){
        return studentreportService.studentDelete(studentreportId,request,response);
    }






}
