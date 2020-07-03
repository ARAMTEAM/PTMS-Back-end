package com.example.demo2.services.impl;

import com.example.demo2.dao.ProjectDao;
import com.example.demo2.dao.ProjectnoticeDao;
import com.example.demo2.dao.StudentDao;
import com.example.demo2.dao.StudentProjectGradeDao;
import com.example.demo2.pojo.*;
import com.example.demo2.response.ResponseResult;
import com.example.demo2.services.ProjectService;
import com.example.demo2.services.ProjectnoticeService;
import com.example.demo2.services.TeacherService;
import com.example.demo2.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.example.demo2.utils.UpdateUtil.getNullPropertyNames;

@Service
@Slf4j
@Transactional
public class ProjectnoticeServiceImpl implements ProjectnoticeService {

    @Autowired
    private ProjectnoticeDao projectnoticeDao;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private StudentProjectGradeDao studentProjectGradeDao;

    /**
     * 查出某项目的所有公告
     *
     * @param projectId
     * @param page
     * @return
     */
    @Override
    public ResponseResult listAllnoticeByProjectId(int projectId, int page) {
        log.info("查询项目  "+projectId+"  的所有公告");
        Project project = projectDao.findByProjectId(projectId);
        if(project == null){
            return ResponseResult.FAILED("项目不存在！");
        }
        Sort sort = Sort.by(Sort.Direction.DESC,"projectnoticeUpdateTime");
        Pageable pageable = PageRequest.of(page -1, Constants.Page.DEFAUT_PAGE_SIZE,sort);
        Page<List<Map<String,Object>>> result = projectnoticeDao.listAllnoticeByProjectId(projectId,pageable);
        log.info("查询成功");
        return ResponseResult.SUCCESS("查询成功").setData(result);
    }

    /**
     * 查出学生所在项目的公告
     *
     * @param studentId
     * @return
     */
    @Override
    public ResponseResult listAllnoticeBystudentId(String studentId) {
        Student student = studentDao.findStudentByStudentId(studentId);
        if(student == null){
            return ResponseResult.FAILED("学生不存在");
        }
        StudentProjectGrade spg = studentProjectGradeDao.findByStudentId(student.getStudentId());
        if(spg == null){
            return ResponseResult.FAILED("学生不在项目中");
        }
        int projectId = spg.getProjectId();
        log.info("查询学生  "+studentId+"  所在项目  "+projectId+"  的全部公告");
        List<Map<String,Object>> result =projectnoticeDao.listAllnoticeByProjectIdNopage(projectId);
        log.info("查询成功");
        return ResponseResult.SUCCESS("查询成功").setData(result);
    }

    /**
     * 教师增加公告
     *
     * @param projectnotice
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult addNotice(Projectnotice projectnotice, HttpServletRequest request, HttpServletResponse response) {
        Teacher teacher = teacherService.checkTeacher(request,response);
        if(teacher == null || teacher.getTeacherId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        Project project = projectDao.findByProjectId(projectnotice.getProjectId());
        if(project == null){
            return ResponseResult.FAILED("项目不存在！");
        }
        if(!project.getTeacherId().equals(teacher.getTeacherId())){
            return ResponseResult.FAILED("不能向不属于自己的项目中添加公告");
        }
        log.info("教师  "+teacher.getTeacherId()+"  正在添加公告到项目  "+projectnotice.getProjectId());
        if (projectnotice.getProjectnoticeTitle() == null || projectnotice.getProjectnoticeContent() == null) {
            return ResponseResult.FAILED("标题和内容不能为空");
        }
        projectnotice.setProjectnoticeCreateTime(new Date());
        projectnotice.setProjectnoticeUpdateTime(new Date());
        log.info("正在添加");
        projectnoticeDao.save(projectnotice);
        log.info("添加成功");
        return ResponseResult.SUCCESS("添加成功");
    }

    /**
     * 教师更新项目公告
     *
     * @param projectnotice
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult update(Projectnotice projectnotice, HttpServletRequest request, HttpServletResponse response) {
        Teacher teacher = teacherService.checkTeacher(request,response);
        if(teacher == null || teacher.getTeacherId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("教师  "+teacher.getTeacherId()+"  正在更新公告  "+projectnotice.getProjectnoticeId());
        Projectnotice projectnoticeById = projectnoticeDao.findByProjectnoticeId(projectnotice.getProjectnoticeId());
        projectnotice.setProjectId(projectnoticeById.getProjectId());
        if(projectnoticeById == null){
            return ResponseResult.FAILED("要更新项目公告不存在");
        }
        Project projectById = projectDao.findByProjectId(projectnoticeById.getProjectId());
        if(!projectById.getTeacherId().equals(teacher.getTeacherId())){
            return ResponseResult.FAILED("不能更新不属于自己项目中的公告");
        }
        log.info("校验通过");
        //检查更新字段
        //检查更新的字段
        BeanUtils.copyProperties(projectnotice, projectnoticeById, getNullPropertyNames(projectnotice));
        projectnoticeById.setProjectnoticeUpdateTime(new Date());
        log.info("开始更新");
        projectnoticeDao.save(projectnoticeById);
        log.info("更新成功");
        return ResponseResult.SUCCESS("更新成功");
    }

    /**
     * 教师删除公告
     *
     * @param projectnoticeId
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult delete(int projectnoticeId, HttpServletRequest request, HttpServletResponse response) {
        Teacher teacher = teacherService.checkTeacher(request,response);
        if(teacher == null || teacher.getTeacherId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("教师  "+teacher.getTeacherId()+"  正在更新公告  "+projectnoticeId);
        Projectnotice projectnoticeById = projectnoticeDao.findByProjectnoticeId(projectnoticeId);
        if(projectnoticeById == null){
            return ResponseResult.FAILED("要删除项目公告不存在");
        }
        Project projectById = projectDao.findByProjectId(projectnoticeById.getProjectId());
        if(!projectById.getTeacherId().equals(teacher.getTeacherId())){
            return ResponseResult.FAILED("不能删除不属于自己项目中的公告");
        }
        log.info("校验通过");
        log.info("开始删除");
        projectnoticeDao.deleteById(projectnoticeId);
        log.info("删除成功");
        return ResponseResult.SUCCESS("删除成功");
    }


}
