package com.example.demo2.services.impl;

import com.example.demo2.dao.*;
import com.example.demo2.pojo.*;
import com.example.demo2.response.ResponseResult;
import com.example.demo2.services.StudentService;
import com.example.demo2.services.StudentreportService;
import com.example.demo2.services.TeacherService;
import com.example.demo2.utils.Constants;
import com.example.demo2.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.example.demo2.utils.UpdateUtil.getNullPropertyNames;

@Service
@Slf4j
@Transactional
public class StudentreportServiceImpl implements StudentreportService {

    @Autowired
    private StudentreportDao studentreportDao;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private StudentProjectGradeDao studentProjectGradeDao;
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private StudentService studentService;
    @Autowired
    private FileDao fileDao;

    /**
     * 教师查看某个学生的报告
     *
     * @param studentId
     * @param page
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult listAllReportByStudentIdToTeacher(String studentId, int page,HttpServletRequest request, HttpServletResponse response) {
        Teacher teacher = teacherService.checkTeacher(request,response);
        if(teacher == null || teacher.getTeacherId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("教师  "+teacher.getTeacherId()+"  正在查询学生  "+studentId+"  的所有报告");
        //检查学生是否存在
        Student studentById = studentDao.findStudentByStudentId(studentId);
        if(studentById == null){
            return ResponseResult.FAILED("学生不存在！");
        }
        //检查学生是否在项目中
        StudentProjectGrade spg = studentProjectGradeDao.findByStudentId(studentId);
        if(spg == null){
            return ResponseResult.FAILED("学生没有在项目中");
        }
        //检查项目是否属于教师
        Project project = projectDao.findByProjectId(spg.getProjectId());
        if(!project.getTeacherId().equals(teacher.getTeacherId())){
            return ResponseResult.FAILED("学生不属于您的项目");
        }
        log.info("检验通过");
        Sort sort = Sort.by(Sort.Direction.DESC,"studentreportDate");
        Pageable pageable = PageRequest.of(page -1, Constants.Page.DEFAUT_PAGE_SIZE,sort);
        Page<List<Map<String,Object>>> result = studentreportDao.listAllReportByStudentId(studentId,pageable);

        return ResponseResult.SUCCESS("查询成功").setData(result);
    }

    /**
     * 学生查看自己的报告
     *
     * @param page
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult listAllReportByStudentIdToStudent(int page, HttpServletRequest request, HttpServletResponse response) {
        //检查学生是否登录
        Student student = studentService.checkStudent(request,response);
        if(student == null || student.getStudentId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //检查是否为本人
        if(!student.getStudentId().equals(student.getStudentId())){
            return ResponseResult.FAILED("不能查询其他人的报告！");
        }
        log.info("校验通过");
        Sort sort = Sort.by(Sort.Direction.DESC,"studentreportDate");
        Pageable pageable = PageRequest.of(page -1, Constants.Page.DEFAUT_PAGE_SIZE,sort);
        Page<List<Map<String,Object>>> result = studentreportDao.listAllReportByStudentId(student.getStudentId(),pageable);

        return ResponseResult.SUCCESS("查询成功").setData(result);
    }

    /**
     * 学生提交报告
     *
     * @param title
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult studentAdd(String title,int type, @RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        //检查学生是否登录
        Student student = studentService.checkStudent(request,response);
        if(student == null || student.getStudentId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //校验通过
        log.info("校验通过");
        Student studentById = studentDao.findStudentByStudentId(student.getStudentId());

        if (!file.isEmpty()) {
            log.info("文件不为空，开始上传");
            // 获取文件名称,包含后缀
            String fileName = file.getOriginalFilename();
            //设置时间戳
            Calendar cal = Calendar.getInstance();
            Date tempDate = new Date();
            cal.setTime(tempDate);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH)+1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            int second = cal.get(Calendar.SECOND);
            String minute1;
            String second1;
            if(minute < 10){
                minute1 = "0"+minute;
            }else {
                minute1 = ""+minute;
            }
            if(second < 10){
                second1 = "0"+second;
            }else {
                second1 = ""+second;
            }


            String timestamp = year+"-"+month+"-"+day+"_"+hour+"-"+minute1+"-"+second1;
            log.info(timestamp);

            //修改文件名
            String newFileName = student.getStudentId()+studentById.getStudentClass()+"_"+timestamp+"_报告"+"_"+fileName;
            // 存放在这个路径下：该路径是该工程目录下的static文件下：(注：该文件可能需要自己创建)
            // 放在static下的原因是，存放的是静态文件资源，即通过浏览器输入本地服务器地址，加文件名时是可以访问到的
            String path = "D://Files/";

            try {
                // 该方法是对文件写入的封装，在util类中，导入该包即可使用，后面会给出方法
                FileUtil.fileupload(file.getBytes(), path, newFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //设置报告的信息
            Studentreport studentreport = new Studentreport();
            studentreport.setStudentreportStatus("未阅");
            studentreport.setStudentId(student.getStudentId());
            studentreport.setStudentreportTitle(title);
            studentreport.setStudentreportFilename(newFileName);
            studentreport.setStudentreportFilepath(path+newFileName);
            studentreport.setStudentreportDate(new Date());
            if(type == 1){
                studentreport.setStudentreportType("进度报告");
            }else if (type == 2){
                studentreport.setStudentreportType("总结报告");
            }else {
                studentreport.setStudentreportType("默认");
            }
            studentreportDao.save(studentreport);
            return ResponseResult.SUCCESS("提交成功");
        }
        return ResponseResult.FAILED("文件为空");
    }

    /**
     * 教师阅读
     *
     * @param studentreportId
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult teacherRead(int studentreportId, HttpServletRequest request, HttpServletResponse response) {
        Teacher teacher = teacherService.checkTeacher(request,response);
        if(teacher == null || teacher.getTeacherId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("教师  "+teacher.getTeacherId()+"  正在标记报告  "+studentreportId+"  为已阅");
        Studentreport studentreportById = studentreportDao.findByStudentreportId(studentreportId);
        if(studentreportById == null){
            return ResponseResult.FAILED("报告不存在！");
        }
        //检查学生是否存在
        Student studentById = studentDao.findStudentByStudentId(studentreportById.getStudentId());
        if(studentById == null){
            return ResponseResult.FAILED("学生不存在！");
        }
        //检查学生是否在项目中
        StudentProjectGrade spg = studentProjectGradeDao.findByStudentId(studentreportById.getStudentId());
        if(spg == null){
            return ResponseResult.FAILED("学生没有在项目中");
        }
        //检查项目是否属于教师
        Project project = projectDao.findByProjectId(spg.getProjectId());
        if(!project.getTeacherId().equals(teacher.getTeacherId())){
            return ResponseResult.FAILED("学生不属于您的项目");
        }
        //开始更新
        studentreportById.setStudentreportStatus("已阅");
        studentreportDao.save(studentreportById);
        log.info("标记成功");
        return ResponseResult.SUCCESS("标记成功");
    }

    /**
     * 学生删除
     *
     * @param studentreportId
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult studentDelete(int studentreportId, HttpServletRequest request, HttpServletResponse response) {
        //检查学生是否登录
        Student student = studentService.checkStudent(request,response);
        if(student == null || student.getStudentId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        Studentreport studentreportById = studentreportDao.findByStudentreportId(studentreportId);
        //检查是否存在
        if(studentreportById == null){
            return ResponseResult.FAILED("文件不存在");
        }
        //检查是否为本人
        if(!student.getStudentId().equals(studentreportById.getStudentId())){
            return ResponseResult.FAILED("不能删除别人报告");
        }
        if(studentreportById.getStudentreportType().equals("已阅")){
            return ResponseResult.FAILED("报告已阅，无法修改");
        }
        log.info("检验成功");
        studentreportDao.deleteById(studentreportId);
        //删除文件
        String path = ""+studentreportById.getStudentreportFilepath();
        log.info(path);
        try{
            FileUtil.deletefile(path);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseResult.SUCCESS("删除成功");
    }

    /**
     * 下载
     *
     * @param request
     * @param response
     * @param studentreportId
     * @return
     * @throws IOException
     */
    @Override
    public String downloadNoPer(HttpServletRequest request,  HttpServletResponse response,int studentreportId) throws IOException {
        Studentreport studentreport = studentreportDao.findByStudentreportId(studentreportId);
        if(studentreport == null){
            return "文件不存在";
        }
        log.info("校验通过");

        Teacher teacher = teacherService.checkTeacher(request,response);
        Student student = studentService.checkStudent(request,response);

        if(student == null && teacher == null){
            return "没登陆";
        }
        if(teacher.getTeacherId() != null && student.getStudentId() == null){
            log.info("教师  "+teacher.getTeacherId()+"  正在下载学生  "+studentreport.getStudentId()+"  的报告  "+studentreport.getStudentreportId());
            StudentProjectGrade studentProjectGrade = studentProjectGradeDao.findByStudentId(studentreport.getStudentId());
            Project project = projectDao.findByProjectId(studentProjectGrade.getProjectId());
            if(!project.getTeacherId().equals(teacher.getTeacherId())){
                log.info("无权限下载");
                return "无权限";
            }
        }else if(teacher.getTeacherId() == null && student.getStudentId() != null){
            log.info("学生  "+student.getStudentId()+"  正在下载学生  "+studentreport.getStudentId()+"  的报告  "+studentreport.getStudentreportId());
            if(!student.getStudentId().equals(studentreport.getStudentId())){
                log.info("无权限下载");
                return "无权限";
            }
        }else {
            return "没登陆";
        }

        log.info("文件名为  "+studentreport.getStudentreportFilename());
        String filename = studentreport.getStudentreportFilename();
        byte[] fileNameBytes = filename.getBytes(StandardCharsets.UTF_8);
        String newfilename = new String(fileNameBytes, 0, fileNameBytes.length, StandardCharsets.ISO_8859_1);
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + newfilename);
        FileUtil.filedownload(response.getOutputStream(),studentreport.getStudentreportFilepath());
        return "s";
    }


}
