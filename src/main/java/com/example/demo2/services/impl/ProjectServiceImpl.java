package com.example.demo2.services.impl;

import com.example.demo2.dao.*;
import com.example.demo2.pojo.*;
import com.example.demo2.response.ResponseResult;
import com.example.demo2.services.JiaowuService;
import com.example.demo2.services.ProjectService;
import com.example.demo2.services.StudentService;
import com.example.demo2.services.TeacherService;
import com.example.demo2.utils.Constants;
import com.example.demo2.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

import static com.example.demo2.utils.UpdateUtil.getNullPropertyNames;

@Service
@Slf4j
@Transactional
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherDao teacherDao;
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private StudentProjectGradeDao studentProjectGradeDao;
    @Autowired
    private JiaowuService jiaowuService;
    @Autowired
    private TrainingDao trainingDao;


    /*
    查询
     */

    /**
     * 列出所有的项目信息
     *
     * @param trainingId
     * @param page
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult listAllProjectByTrainingId(int trainingId, int page,HttpServletRequest request, HttpServletResponse response) {
        log.info("查询出实训  "+trainingId+"  中的所有项目");
        Pageable pageable = PageRequest.of(page -1, Constants.Page.DEFAUT_PAGE_SIZE);
        Page<List<Map<String,Object>>> all = projectDao.listAllProjectByTrainingId(trainingId,pageable);
        log.info("查询成功");
        return ResponseResult.SUCCESS("查询成功！").setData(all);
    }

    /**
     * 所有项目不分页
     *
     * @param trainingId
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult listAllProjectByTrainingIdNoPage(Integer trainingId, HttpServletRequest request, HttpServletResponse response) {
        log.info("查询出实训  "+trainingId+"  中的所有项目");
        List<Map<String,Object>> all = projectDao.listAllProjectByTrainingIdNoPage(trainingId);
        log.info("查询成功");
        return ResponseResult.SUCCESS("查询成功！").setData(all);
    }

    /**
     * 列出所有教师项目的信息
     *
     * @param trainingId
     * @param page
     * @param request
     * @param response
     * @return
     */
    public ResponseResult listAllTeacherProjectByTrainingId(Integer trainingId, Integer page,HttpServletRequest request, HttpServletResponse response) {
        log.info("查询所有的教师项目");
        Pageable pageable = PageRequest.of(page -1, Constants.Page.DEFAUT_PAGE_SIZE);
        Page<List<Map<String,Object>>> all = projectDao.listAllTeacherProjectByTrainingId(trainingId,pageable);
        log.info("查询成功");
        return ResponseResult.SUCCESS("查询成功！").setData(all);
    }

    /**
     * 列出某个教师名下所有的项目信息
     *
     * @param teacherId
     * @param page
     * @return
     */
    @Override
    public ResponseResult listAllByTeacherId(String teacherId,int page) {
        log.info("查询教师  "+teacherId+"  的所有项目");
        Teacher teacher = teacherDao.findTeacherByTeacherId(teacherId);
        if(teacher == null){
            log.info("教师不存在");
            return ResponseResult.FAILED("教师不存在");
        }
        Pageable pageable = PageRequest.of(page -1, Constants.Page.DEFAUT_PAGE_SIZE);
        Page<List<Map<String,Object>>> result = projectDao.listAllByTeacherId(teacherId,pageable);
        log.info("查询成功");
        return ResponseResult.SUCCESS("查询成功").setData(result);
    }


    /**
     * 列出某个教师下需要审核的项目
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult listAllTeacherNeedReceive(HttpServletRequest request, HttpServletResponse response) {
        Teacher teacher = teacherService.checkTeacher(request,response);
        if(teacher == null || teacher.getTeacherId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("教师  "+teacher.getTeacherId()+"  正在查询他未审核的创新项目");
        List<Map<String,Object>> result = projectDao.listAllTeacherNeedReceive(teacher.getTeacherId());
        log.info("查询成功");
        return ResponseResult.SUCCESS("查询成功").setData(result);
    }

    /**
     * 查询出教务下所有未审核的项目
     *
     * @param page
     * @return
     */
    @Override
    public ResponseResult listAllNoAuditProject(int page,HttpServletRequest request,HttpServletResponse response) {
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(jiaowu == null || jiaowu.getJiaowuId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("教务  "+jiaowu.getJiaowuId()+"  正在查询所有待审核的项目");
        Pageable pageable = PageRequest.of(page -1, Constants.Page.DEFAUT_PAGE_SIZE);
        Page<List<Map<String,Object>>> result = projectDao.listAllNoAuditProject(jiaowu.getJiaowuId(),pageable);
        log.info("查询成功");
        return ResponseResult.SUCCESS("查询成功").setData(result);
    }

    /**
     * 查询出教务下所有需答辩的项目
     *
     * @param page
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult listAllNeedDebateProject(int page, HttpServletRequest request, HttpServletResponse response) {
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(jiaowu == null || jiaowu.getJiaowuId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("教务  "+jiaowu.getJiaowuId()+"  正在查询所有需答辩的项目");
        Pageable pageable = PageRequest.of(page -1, Constants.Page.DEFAUT_PAGE_SIZE);
        Page<List<Map<String,Object>>> result = projectDao.listAllNeedDebateProject(jiaowu.getJiaowuId(),pageable);
        log.info("查询成功");
        return ResponseResult.SUCCESS("查询成功").setData(result);
    }

    /**
     * 查询单个项目
     *
     * @param projectId
     * @return
     */
    @Override
    public ResponseResult listOneByProjectId(int projectId) {
        Project project = projectDao.findByProjectId(projectId);
        if(project != null){
            return ResponseResult.SUCCESS("查询成功").setData(project);
        }
        return ResponseResult.FAILED("项目不存在");
    }




    /*
    更新
     */

    /**
     * 教师修改未通过的项目信息
     *
     * @param project
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult TeacherUpdate(Project project, HttpServletRequest request, HttpServletResponse response) {
        //检验教师是否登录，并且教师只能往自己所在的实训中增加项目
        Teacher teacher = teacherService.checkTeacher(request,response);
        if(teacher == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(teacher.getTeacherId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //查出原来的项目
        Project projectById = projectDao.findByProjectId(project.getProjectId());
        log.info("教师  "+teacher.getTeacherId()+"  正在修改项目  "+project.getProjectId());
        if(!projectById.getTeacherId().equals(teacher.getTeacherId())){
            return ResponseResult.FAILED("不能修改不属于自己的项目！");
        }else{
            if(!projectById.getProjectApplicantType().equals(Constants.User.UserType_teacher)){
                return ResponseResult.FAILED("不能修改学生申请的项目！");
            }else{
                log.info(projectById.getProjectStatus());
                if(!projectById.getProjectStatus().equals(Constants.Status.STATUS1) && !projectById.getProjectStatus().equals(Constants.Status.STATUS4)){
                    return ResponseResult.FAILED("只能修改待审核和未通过的项目！");
                }
            }
        }
        //检查完成，可以修改
        //检查字段
        if(!project.getProjectApplicantType().equals(projectById.getProjectApplicantType())){
            return ResponseResult.FAILED("不可以修改审核状态");
        }
        //检查更新的字段
        BeanUtils.copyProperties(project, projectById, getNullPropertyNames(project));
        //存入数据库
        projectDao.save(projectById);
        log.info("修改成功");
        return ResponseResult.SUCCESS("修改成功").setData(projectById);
    }

    /**
     * 教师通过指导教师请求
     *
     * @param projectId
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult TeacherReceiveOrNot(int projectId,int reply, HttpServletRequest request, HttpServletResponse response) {
        Teacher teacher = teacherService.checkTeacher(request,response);
        if(teacher == null || teacher.getTeacherId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("教师  "+teacher.getTeacherId()+"  正在审核项目  "+projectId+"  的邀请");
        Project projectById = projectDao.findByProjectId(projectId);
        if(projectById == null){
            return ResponseResult.FAILED("项目不存在");
        }
        if(!projectById.getTeacherId().equals(teacher.getTeacherId())){
            return ResponseResult.FAILED("此项目的指导教师不是您");
        }
        log.info("校验通过");
        if(reply == 1){//1代表通过
            projectById.setProjectStatus(Constants.Status.STATUS1);
        }else {
            projectById.setProjectStatus(Constants.Status.STATUS6);
        }
        //存入数据库
        projectDao.save(projectById);
        log.info("审核成功");
        return ResponseResult.SUCCESS("操作成功");
    }


    /**
     * 教务更新项目
     *
     * @param project
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult jiaowuUpdate(Project project, HttpServletRequest request, HttpServletResponse response) {
        //检查教务权限
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(jiaowu == null || jiaowu.getJiaowuId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("教务  "+jiaowu.getJiaowuId()+"  正在更新项目  "+project.getProjectId());
        //查出原来的
        Project projectById = projectDao.findByProjectId(project.getProjectId());
        if(projectById == null){
            log.info("项目不存在");
            return ResponseResult.FAILED("没有这个项目");
        }
        //检查是否归教务管
        Training training2 = trainingDao.findByTrainingId(projectById.getTrainingId());
        if(!training2.getJiaowuId().equals(jiaowu.getJiaowuId())){
            return ResponseResult.FAILED("操作有误!不能操作不属于自己实训的项目");
        }
        //检查要更新的字段
        project.setTrainingId(projectById.getTrainingId());
        BeanUtils.copyProperties(project, projectById, getNullPropertyNames(project));
        //检查字段
        if(!checkText(projectById)){
            log.info("字段不合法");
            return ResponseResult.FAILED("字段不合法");
        }
        if(!checkStatus(projectById)){
            log.info("状态不合法");
            return ResponseResult.FAILED("状态不合法");
        }
        log.info("检查完成,开始更新");
        projectDao.save(projectById);
        log.info("更新成功");
        log.info("");
        log.info("");
        log.info("");
        return ResponseResult.SUCCESS("更新成功");
    }


    /*
    增加
     */


    /**
     * 教务添加项目
     *
     * @param project
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult jiaowuAddProject(Project project, HttpServletRequest request, HttpServletResponse response) {
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(jiaowu == null || jiaowu.getJiaowuId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("教务  "+jiaowu.getJiaowuId()+"  正在添加项目  "+project.getProjectName()+"  到实训  "+project.getTrainingId());
        Training trainingByProject = trainingDao.findByTrainingId(project.getTrainingId());
        if(trainingByProject == null){
            return ResponseResult.FAILED("实训ID不存在!");
        }
        if(!trainingByProject.getJiaowuId().equals(jiaowu.getJiaowuId())){
            return ResponseResult.FAILED("项目中实训ID有误!");
        }
        //不可以指定ID
        log.info(""+project.getProjectId());
        if(project.getProjectId() != 0){
            return ResponseResult.FAILED("不可以指定ID");
        }
        //检查教师
        Teacher teacher = teacherDao.findTeacherByTeacherId(project.getTeacherId());
        if(teacher == null){
            return ResponseResult.FAILED("教师不存在");
        }
        Training trainingByTeacher = trainingDao.findByTrainingId(teacher.getTrainingId());
        if(!trainingByTeacher.getJiaowuId().equals(jiaowu.getJiaowuId())){
            return ResponseResult.FAILED("教师不属于这个实训");
        }
        if(teacher.getTrainingId() != project.getTrainingId()){
            return ResponseResult.FAILED("教师不属于这个实训");
        }
        log.info("校验通过");
        project.setProjectApplicantType(Constants.User.UserType_teacher);
        project.setProjectStatus(Constants.Status.STATUS2);
        log.info("正在添加");
        projectDao.save(project);
        log.info("添加成功");
        return ResponseResult.SUCCESS("添加成功!");
    }



    /**
     * 教师申请项目
     *
     * @param project
     * @param request
     * @param response
     * @return
     */
    public ResponseResult TeacheraddProject(Project project,HttpServletRequest request,HttpServletResponse response){
        //检验教师是否登录，并且教师只能往自己所在的实训中增加项目
        Teacher teacher = teacherService.checkTeacher(request,response);
        if(teacher == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(teacher.getTeacherId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }

        log.info("教师  "+teacher.getTeacherId()+"  正在准备创建教师项目  "+project.getProjectName());
        //检查字段
        if(!checkText(project)){
            return ResponseResult.FAILED("字段错误!");
        }
        //检查实训ID是否是教师所属的实训
        int trainingIdFromProject = project.getTrainingId();
        Teacher teacherById = teacherDao.findTeacherByTeacherId(teacher.getTeacherId());
        if(trainingIdFromProject != teacherById.getTrainingId()){
            log.info("实训ID错误");
            return ResponseResult.FAILED("操作有误!请检查实训ID");
        }
        //检查登录的教师和提交项目的教师是否一致
        if(!teacher.getTeacherId().equals(project.getTeacherId())){
            return ResponseResult.FAILED("操作有误!请检查教师ID");
        }
        log.info("校验通过");
        project.setProjectApplicantType("教师");
        project.setProjectStatus("待审核");
        log.info("正在添加");
        projectDao.save(project);
        log.info("添加成功");
        log.info("");
        return ResponseResult.SUCCESS("申请实训成功!");
    }

    /**
     * 学生申请项目
     *
     * @param project
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult StudentaddProject(Project project, HttpServletRequest request, HttpServletResponse response) {
        //校验学生登录
        Student student = studentService.checkStudent(request,response);
        if(student == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(student.getStudentId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("学生  "+student.getStudentId()+"  正在申请创新实训  "+project.getProjectName());
        //检查学生是否已经存在于一个项目中
        StudentProjectGrade spg = studentProjectGradeDao.findByStudentId(student.getStudentId());
        if(spg != null){
            log.info("申请人已经存在于一个项目中,不可再次申请");
            return ResponseResult.FAILED("申请人已经存在于一个项目中,不可再次申请!");
        }
        //不可以指定ID
        log.info(""+project.getProjectId());
        if(project.getProjectId() != 0){
            return ResponseResult.FAILED("不可以指定ID");
        }
        //检查实训ID是否是学生所属的实训
        int trainingIdFromProject = project.getTrainingId();
        Student studentById = studentDao.findStudentByStudentId(student.getStudentId());
        if(trainingIdFromProject != studentById.getTrainingId()){
            log.info("实训ID错误");
            return ResponseResult.FAILED("操作有误!请检查实训ID");
        }
        log.info("校验通过");
        //设置默认值
        project.setProjectApplicantType("学生"+student.getStudentId());
        project.setProjectStatus(Constants.Status.STATUS5); //设置状态为等待教师审核
        project.setProjectMaxNum(6);
        project.setProjectGroupNum(1);
        log.info("正在添加");
        projectDao.save(project);
        log.info("添加成功");
        //把学生添加到小组列表
        //获取刚刚存入的项目的项目ID
        int projectId = projectDao.findByProjectApplicantType("学生"+student.getStudentId()).getProjectId();
        log.info("已经添加到了项目表,项目ID为 == > "+projectId);
        //构建实体类
        StudentProjectGrade spgtoSave = new StudentProjectGrade();
        spgtoSave.setStudentId(studentById.getStudentId());
        spgtoSave.setProjectId(projectId);
        spgtoSave.setGrades5Points(-1);
        spgtoSave.setGrades100Points(-1);
        spgtoSave.setIsjointed(1);
        spgtoSave.setTrainingId(studentById.getTrainingId());
        spgtoSave.setIsleader(1);
        //存入相应的表
        studentProjectGradeDao.save(spgtoSave);
        log.info("已经添加到了学生_项目表");
        log.info("");
        //返回结果
        return ResponseResult.SUCCESS("创建项目成功");
    }


    /**
     * 教务删除
     *
     * @param projectId
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult jiaowuDelete(int projectId,HttpServletRequest request, HttpServletResponse response) {
        //检查教务权限
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(jiaowu == null || jiaowu.getJiaowuId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("教务  "+jiaowu.getJiaowuId()+"  正在删除项目  "+projectId);
        //查出原来的
        Project projectById = projectDao.findByProjectId(projectId);
        if(projectById == null){
            log.info("删除的项目不存在");
            return ResponseResult.FAILED("删除的项目不存在");
        }
        //检查是否归教务管
        Training training = trainingDao.findByTrainingId(projectById.getTrainingId());
        if(!training.getJiaowuId().equals(jiaowu.getJiaowuId())){
            log.info("项目不属于教务,无法删除");
            return ResponseResult.FAILED("操作有误!不能操作不属于自己实训的项目");
        }
        //开始删除
        projectDao.deleteById(projectId);
        log.info("删除成功");
        return ResponseResult.SUCCESS("删除成功");
    }




    //project中的各项不能为空  名称  最大人数  实训ID  审核状态
    boolean checkText(Project project){
        if(TextUtils.isEmpty(project.getProjectName()) || TextUtils.isEmpty(project.getTeacherId())  || project.getTrainingId() < 1){
            return false;
        }else {
            return true;
        }
    }

    //检查状态
    boolean checkStatus(Project project){
        String statu = project.getProjectStatus();
        if(statu.equals(Constants.Status.STATUS1) || statu.equals(Constants.Status.STATUS2) || statu.equals(Constants.Status.STATUS3) || statu.equals(Constants.Status.STATUS4)){
            return true;
        }
        return false;
    }




}
