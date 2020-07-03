package com.example.demo2.services.impl;

import com.example.demo2.dao.*;
import com.example.demo2.pojo.*;
import com.example.demo2.response.ResponseResult;
import com.example.demo2.services.JiaowuService;
import com.example.demo2.services.StudentProjectGradeService;
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
public class StudentProjectGradeServiceImpl implements StudentProjectGradeService {

    @Autowired
    private StudentProjectGradeDao studentProjectGradeDao;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherDao teacherDao;
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private JiaowuService jiaowuService;
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private TrainingDao trainingDao;

    /**
     * 学生创建项目或邀请
     *
     * @param spjg
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult StudentAdd(StudentProjectGrade spjg, HttpServletRequest request, HttpServletResponse response) {
        Student student = studentService.checkStudent(request,response);
        if(student == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(student.getStudentId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("检验学生权限完成");
        log.info("学生  "+student.getStudentId() +"  想要把学生  "+spjg.getStudentId() +"  添加到项目  "+spjg.getProjectId() +"  中");
        //检查要添加的学生是否存在
        Student studentToAdd = studentDao.findStudentByStudentId(spjg.getStudentId());
        if(studentToAdd == null){
            return ResponseResult.FAILED("要添加的学生不存在!");
        }
        //检查学生是否已经加入了一个项目
        StudentProjectGrade spjgById = studentProjectGradeDao.findByStudentId(spjg.getStudentId());
        if(spjgById != null){
            log.info("学生已存在于某个项目中或已经被邀请过");
            return ResponseResult.FAILED("该学生已经位于一个项目中或已经被邀请过!请勿重复添加!");
        }
        //检查实训ID
        if(studentToAdd.getTrainingId() != spjg.getTrainingId()){
            log.info("实训编号检查出错");
            return ResponseResult.FAILED("该学生不属于这个实训");
        }
        //检查项目ID是否是由自己申请的
        Project project = projectDao.findByProjectId(spjg.getProjectId());
        if(!project.getProjectApplicantType().equals("学生"+student.getStudentId())){
            log.info("不是队长");
            return ResponseResult.FAILED("您不是这个项目的队长,无法添加!");
        }
        //检查项目人数是否已经超过最大人数限制
        Map<String,Object> map = studentProjectGradeDao.SumNumOfProject(spjg.getProjectId());
        log.info("项目  "+spjg.getProjectId()+"  当前人数为:  "+ map.get("SumNum"));
        if( Integer.valueOf(map.get("SumNum").toString()) >= project.getProjectMaxNum()){
            return ResponseResult.FAILED("队伍人数已到达最大限制,如有疑问请联系教务!");
        }
        log.info("设置队长和加入状态");
        if(student.getStudentId().equals(spjg.getStudentId())){
            spjg.setIsleader(1);
            spjg.setIsjointed(1);
        }else {
            spjg.setIsleader(0);
            spjg.setIsjointed(0);
        }
        log.info("设置分数成绩默认值");
        spjg.setGrades100Points(-1);
        spjg.setGrades5Points(-1);
        studentProjectGradeDao.save(spjg);
        if(student.getStudentId().equals(spjg.getStudentId())){
            return ResponseResult.SUCCESS("添加成功");
        }else {
            return ResponseResult.SUCCESS("发送邀请成功");
        }

    }

    /**
     * 教师创建项目时添加预定学生,不用再参与志愿填报
     *
     * @param spjg
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult TeacherAdd(StudentProjectGrade spjg, HttpServletRequest request, HttpServletResponse response) {
        Teacher teacher = teacherService.checkTeacher(request,response);
        if(teacher == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(teacher.getTeacherId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("检验教师权限完成");
        Project projectById = projectDao.findByProjectId(spjg.getProjectId());
        if(!projectById.getTeacherId().equals(teacher.getTeacherId()) || !projectById.getProjectApplicantType().equals("教师")){
            return ResponseResult.FAILED("不能把学生添加到别人的项目中！");
        }
        Student studentById = studentDao.findStudentByStudentId(spjg.getStudentId());
        if(studentById == null){
            log.info("学生不存在");
            return ResponseResult.FAILED("学生不存在");
        }
        if(studentById.getTrainingId() != projectById.getTrainingId()){
            log.info("学生不属于这个实训");
            return ResponseResult.FAILED("学生不属于这个实训");
        }
        StudentProjectGrade spgById = studentProjectGradeDao.findByStudentId(spjg.getStudentId());
        if(spgById != null){
            log.info("学生已经存在于一个项目中");
            return ResponseResult.FAILED("学生已经存在于一个项目中");
        }
        spjg.setTrainingId(projectById.getTrainingId());
        spjg.setGrades100Points(-1);
        spjg.setGrades5Points(-1);
        spjg.setIsjointed(1);
        studentProjectGradeDao.save(spjg);
        return ResponseResult.SUCCESS("添加初始学生成功!");
    }

    /**
     * 教务添加
     *
     * @param spjg
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult JiaowuAdd(StudentProjectGrade spjg, HttpServletRequest request, HttpServletResponse response) {
        //检查教务是否登录了
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(jiaowu == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(jiaowu.getJiaowuId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //检查项目所在实训是否归这个教务管
        Project projectById = projectDao.findByProjectId(spjg.getProjectId());
        Training trainingById = trainingDao.findByTrainingId(projectById.getTrainingId());
        if(!trainingById.getJiaowuId().equals(jiaowu.getJiaowuId())){
            log.info("不能添加到不属于自己的实训");
            return ResponseResult.FAILED("不能添加到不属于自己的实训");
        }
        //检查学生是否已经存在一个项目了
        StudentProjectGrade spgById = studentProjectGradeDao.findByStudentId(spjg.getStudentId());
        if(spgById != null){
            log.info("学生已经存在于一个项目中");
            return ResponseResult.FAILED("学生已经存在于一个项目中!");
        }
        //检查完成,开始添加
        spjg.setIsjointed(1);
        spjg.setGrades5Points(-1);
        spjg.setGrades100Points(-1);

        return null;
    }
    /**
     * 学生拒绝申请
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult studentReceive( HttpServletRequest request, HttpServletResponse response) {
        //检查学生是否登录
        Student student = studentService.checkStudent(request,response);
        if(student == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(student.getStudentId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("学生  "+student.getStudentId()+"  正在拒绝项目邀请");

        //检查是否存在记录
        StudentProjectGrade spg = studentProjectGradeDao.findByStudentId(student.getStudentId());
        if(spg == null){
            return ResponseResult.FAILED("不存在邀请");
        }
        if(spg.getIsjointed() == 1){
            log.info("已经接受邀请");
            return ResponseResult.FAILED("已经接受邀请!");
        }
        spg.setIsjointed(1);
        studentProjectGradeDao.save(spg);
        return ResponseResult.SUCCESS("接受邀请成功!");
    }

    /**
     * 学生拒绝申请
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult studentReject( HttpServletRequest request, HttpServletResponse response) {
        //检查学生是否登录
        Student student = studentService.checkStudent(request,response);
        if(student == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(student.getStudentId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("学生  "+student.getStudentId()+"  正在拒绝项目邀请");

        //检查是否存在记录
        StudentProjectGrade spg = studentProjectGradeDao.findByStudentId(student.getStudentId());
        if(spg == null){
            return ResponseResult.FAILED("不存在邀请");
        }
        if(spg.getIsjointed() == 1){
            log.info("不可自行退出");
            return ResponseResult.FAILED("不可自行退出!");
        }
        //检查完成,开始删除
        studentProjectGradeDao.deleteById(student.getStudentId());
        return ResponseResult.SUCCESS("拒绝邀请成功!");
    }

    /**
     * 教务删除
     *
     * @param studentId
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult jiaowuDelete(String studentId, HttpServletRequest request, HttpServletResponse response) {
        log.info("教务删除");
        //检查教务是否登录了
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(jiaowu == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(jiaowu.getJiaowuId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //检查学生是否有条目
        StudentProjectGrade spjg = studentProjectGradeDao.findByStudentId(studentId);
        if(spjg == null){
            log.info("学生不存在于项目中");
            return ResponseResult.FAILED("学生不存在于项目中");
        }
        log.info("教务  "+jiaowu.getJiaowuId()+"  准备把学生  "+studentId+"  从所在项目删除");
        //检查项目所在实训是否归这个教务管
        Project projectById = projectDao.findByProjectId(spjg.getProjectId());
        Training trainingById = trainingDao.findByTrainingId(projectById.getTrainingId());
        if(!trainingById.getJiaowuId().equals(jiaowu.getJiaowuId())){
            log.info("不能管辖不属于自己的实训");
            return ResponseResult.FAILED("不能管辖到不属于自己的实训");
        }
        //检查完成,开始删除
        studentProjectGradeDao.deleteById(studentId);
        return ResponseResult.SUCCESS("删除成功!");
    }


    /*
    查询
     */

    /**
     * 查询某项目的成员列表
     *
     * @param projectId
     * @return
     */
    @Override
    public ResponseResult listAllItemByProjectId(Integer projectId) {
        log.info("查询项目  "+projectId+"  的成员列表");
        List<Map<String,Object>> result = studentProjectGradeDao.listAllItemByProjectId(projectId);
        return ResponseResult.SUCCESS("查询成功!").setData(result);
    }

    /**
     * 查出某个学生的项目情况
     *
     * @param studentId
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult listOneItemByStudent(String studentId,HttpServletRequest request, HttpServletResponse response) {
        //需要学生或老师权限
        log.info("查询学生  "+studentId+"  的项目情况");
        Student student = studentService.checkStudent(request,response);
        Teacher teacher = teacherService.checkTeacher(request,response);
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(student == null && student == null && jiaowu == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        Student studentById = studentDao.findStudentByStudentId(studentId);
        if(studentById == null){
            return ResponseResult.FAILED("要查询的学生不存在");
        }
        if(student.getStudentId() == null && teacher.getTeacherId() != null && jiaowu .getJiaowuId() == null){
            //教师查看
            StudentProjectGrade spg = studentProjectGradeDao.findByStudentId(studentId);
            Project project = projectDao.findByProjectId(spg.getProjectId());
            if(!project.getTeacherId().equals(teacher.getTeacherId())){
                return ResponseResult.FAILED("学生不属于您的项目！");
            }
            log.info("开始查询");
            Map map = studentProjectGradeDao.findProjectMessageStudentId(studentId);
            return ResponseResult.SUCCESS("查询成功").setData(map);
        }
        if(student.getStudentId() != null && teacher.getTeacherId() == null && jiaowu.getJiaowuId() == null){
            //学生本人查看
            //检查学生是否一致
            if(!studentId.equals(student.getStudentId())){
                log.info("学生不对应");
                return ResponseResult.FAILED("不能查询其他人的情况!");
            }
            log.info("开始查询");
            Map map = studentProjectGradeDao.findProjectMessageStudentId(studentId);
            log.info("查询成功");
            return ResponseResult.SUCCESS("查询成功").setData(map);
        }
        if(jiaowu.getJiaowuId() != null){
            log.info("开始查询");
            Map map = studentProjectGradeDao.findProjectMessageStudentId(studentId);
            log.info("查询成功");
            return ResponseResult.SUCCESS("查询成功").setData(map);
        }
        return ResponseResult.ACCOUNT_NOT_LOGIN();
    }

    /**
     * 列出所有不在项目中的学生
     *
     * @param trainingId
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult listNoProjectStudent(int trainingId,int page, HttpServletRequest request, HttpServletResponse response) {
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(jiaowu == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(jiaowu.getJiaowuId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("教务  "+jiaowu.getJiaowuId()+"  正在查询实训  "+trainingId+"  中的所有不在项目中的学生");
        Training training = trainingDao.findByTrainingId(trainingId);
        if(training == null){
            return ResponseResult.FAILED("实训ID不正确");
        }
        if(!training.getJiaowuId().equals(jiaowu.getJiaowuId())){
            return ResponseResult.FAILED("无权查看该实训");
        }
        log.info("校验完成");
        Pageable pageable = PageRequest.of(page -1, Constants.Page.DEFAUT_PAGE_SIZE);
        Page<List<Map<String,Object>>> result = studentProjectGradeDao.listNoProjectStudent(trainingId,pageable);
        log.info("查询成功");
        return ResponseResult.SUCCESS("查询成功").setData(result);
    }

    /**
     * 教务导出成绩
     *
     * @param trainingId
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult GradeOutput(int trainingId, HttpServletRequest request, HttpServletResponse response) {
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(jiaowu == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(jiaowu.getJiaowuId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("教务  "+jiaowu.getJiaowuId()+"  正在导出实训  "+trainingId+"  中的成绩");
        Training training = trainingDao.findByTrainingId(trainingId);
        if(training == null){
            return ResponseResult.FAILED("实训ID不正确");
        }
        if(!training.getJiaowuId().equals(jiaowu.getJiaowuId())){
            return ResponseResult.FAILED("无权操作该实训");
        }
        log.info("校验成功");
        List<Map<String,Object>> result = studentProjectGradeDao.GradeOutPut(trainingId);
        log.info("导出成功");
        return ResponseResult.SUCCESS("导出成功").setData(result);
    }


    /**
     * 教师更新成绩（百分制）
     *
     * @param studentId
     * @param grade
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult TeacherUpdate100Grade(String studentId, int grade, HttpServletRequest request, HttpServletResponse response) {
        //验证教师登录
        Teacher teacher = teacherService.checkTeacher(request,response);
        if(teacher == null || teacher.getTeacherId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("教师  "+teacher.getTeacherId() +"  正在把学生的百分制  "+studentId+"  的成绩设置为  "+grade);
        //验证学生是否属于教师的项目中
        StudentProjectGrade spg = studentProjectGradeDao.findByStudentId(studentId);
        Project projectById = projectDao.findByProjectId(spg.getProjectId());
        if(!projectById.getTeacherId().equals(teacher.getTeacherId())){
            log.info("学生不属于登录的老师，无法更新成绩");
            return ResponseResult.FAILED("学生不属于您的项目");
        }
        //检验成绩是否为初始值,成绩默认只可设置一次
        if(spg.getGrades100Points() != -1 ){
            log.info("成绩不是默认值,无法更新");
            return ResponseResult.FAILED("学生的百分制成绩已经被更新过,如有疑问请联系教务");
        }
        //开始更新成绩
        if(!TextUtils.Grade100IsLeagal(grade)){
            return ResponseResult.FAILED("成绩格式不正确");
        }
        studentProjectGradeDao.TeacherUpdate100Grade(studentId, grade);
        return ResponseResult.SUCCESS("更新成功");
    }


    /**
     * 教师更新成绩（5分制）
     *
     * @param studentId
     * @param grade
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult TeacherUpdate5Grade(String studentId, double grade, HttpServletRequest request, HttpServletResponse response) {
        //验证教师登录
        Teacher teacher = teacherService.checkTeacher(request,response);
        if(teacher == null || teacher.getTeacherId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("教师  "+teacher.getTeacherId() +"  正在把学生的5分制  "+studentId+"  的成绩设置为  "+grade);
        //验证学生是否属于教师的项目中
        StudentProjectGrade spg = studentProjectGradeDao.findByStudentId(studentId);
        Project projectById = projectDao.findByProjectId(spg.getProjectId());
        if(!projectById.getTeacherId().equals(teacher.getTeacherId())){
            log.info("学生不属于登录的老师，无法更新成绩");
            return ResponseResult.FAILED("学生不属于您的项目");
        }
        //检验成绩是否为初始值,成绩默认只可设置一次
        if(spg.getGrades5Points() != -1 ){
            log.info("成绩不是默认值,无法更新");
            return ResponseResult.FAILED("学生的5分制成绩已经被更新过,如有疑问请联系教务");
        }
        //开始更新成绩
        if(!TextUtils.Grade5IsLeagal(grade)){
            return ResponseResult.FAILED("成绩格式不正确");
        }
        studentProjectGradeDao.TeacherUpdate5Grade(studentId, grade);
        return ResponseResult.SUCCESS("更新成功");
    }

    /**
     * 教务更新
     *
     * @param spg
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult JiaowuUpdate(StudentProjectGrade spg, HttpServletRequest request, HttpServletResponse response) {
        log.info("教务更新");
        //检查教务是否登录了
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(jiaowu == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(jiaowu.getJiaowuId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("教务  "+jiaowu.getJiaowuId()+"  正在更新学生  "+spg.getStudentId()+"  的所在项目等信息");
        //检查教务的ID和学生所属实训ID是否一致
        Student student = studentDao.findStudentByStudentId(spg.getStudentId());
        Training training = trainingDao.findByTrainingId(student.getTrainingId());
        if(!training.getJiaowuId().equals(jiaowu.getJiaowuId())){
            log.info("学生不属于教务");
            return ResponseResult.FAILED("不能操作不属于自己实训的学生");
        }
        //检查更新字段
        StudentProjectGrade spgById = studentProjectGradeDao.findByStudentId(spg.getStudentId());
        BeanUtils.copyProperties(spg, spgById, getNullPropertyNames(spg));
        log.info("开始更新");
        //检查项目所在实训是否归这个教务管
        Project projectById = projectDao.findByProjectId(spg.getProjectId());
        Training trainingById = trainingDao.findByTrainingId(projectById.getTrainingId());
        if(!trainingById.getJiaowuId().equals(jiaowu.getJiaowuId())){
            log.info("不能添加到不属于自己的实训");
            return ResponseResult.FAILED("不能添加到不属于自己的实训");
        }
        //检查成绩格式
        if(!TextUtils.Grade100IsLeagal(spg.getGrades100Points()) || !TextUtils.Grade5IsLeagal(spg.getGrades5Points())){
            return ResponseResult.FAILED("成绩格式不正确");
        }
        log.info("更新成功");
        return ResponseResult.SUCCESS("更新成功");
    }



}
