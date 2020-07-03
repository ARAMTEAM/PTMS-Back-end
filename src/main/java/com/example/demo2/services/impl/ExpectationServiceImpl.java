package com.example.demo2.services.impl;

import com.example.demo2.dao.*;
import com.example.demo2.pojo.*;
import com.example.demo2.response.ResponseResult;
import com.example.demo2.services.ExpectationService;
import com.example.demo2.services.JiaowuService;
import com.example.demo2.services.StudentService;
import com.example.demo2.utils.AllocationUtils;
import com.example.demo2.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.*;

@Service
@Slf4j
@Transactional
public class ExpectationServiceImpl implements ExpectationService {

    @Autowired
    private ExpectationDao expectationDao;
    @Autowired
    private JiaowuService jiaowuService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TrainingDao trainingDao;
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private StudentProjectGradeDao studentProjectGradeDao;

    /**
     * 分页列出所有学生志愿，需要教务对当前实训有操作权
     *
     * @param trainingId
     * @param page
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult listAllE(int trainingId,int page, HttpServletRequest request, HttpServletResponse response) {
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(jiaowu == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(jiaowu.getJiaowuId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        String jiaowuId = jiaowu.getJiaowuId();
        log.info("教务  "+jiaowuId+"  正在查询所有学生的志愿");
        String jiaowuIdBytrainingId = trainingDao.findByTrainingId(trainingId).getJiaowuId();
        if(!jiaowuId.equals(jiaowuIdBytrainingId)){
            return ResponseResult.FAILED("不能查询不属于自己实训的学生志愿！");
        }
        log.info("登录校验通过，查询学生志愿****************");
        Pageable pageable = PageRequest.of(page -1, Constants.Page.DEFAUT_PAGE_SIZE);
        Page<List<Map<String,Object>>> all = expectationDao.listAllE(trainingId,pageable);

        return ResponseResult.SUCCESS("查询志愿成功!").setData(all);
    }

    @Override
    public ResponseResult JiaowulistOneE(String studentId, HttpServletRequest request, HttpServletResponse response) {
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(jiaowu == null || jiaowu.getJiaowuId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("教务  "+jiaowu.getJiaowuId()+"  正在查询学生  "+studentId+"  的志愿");
        Student studentById = studentDao.findStudentByStudentId(studentId);
        if(studentById == null){
            return ResponseResult.FAILED("学生不存在");
        }
        Training trainingById = trainingDao.findByTrainingId(studentById.getTrainingId());
        if(!trainingById.getJiaowuId().equals(jiaowu.getJiaowuId())){
            return ResponseResult.FAILED("无权查看");
        }
        log.info("校验通过");
        Map<String,Object> result = expectationDao.listOneE(studentId);
        log.info("查询成功");
        return ResponseResult.SUCCESS("查询成功").setData(result);
    }

    /**
     * 列出单个学生志愿
     *
     * @param studentId
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult listOneE( String studentId, HttpServletRequest request, HttpServletResponse response) {
        //只允许学生本人访问
        Student student = studentService.checkStudent(request,response);
        if(student == null || student.getStudentId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(!studentId.equals(student.getStudentId())){
            return ResponseResult.FAILED("操作有误!");
        }
        log.info("登录校验通过，查询学生志愿****************");
        Map<String,Object> result = expectationDao.listOneE(studentId);
        return ResponseResult.SUCCESS("查询志愿成功!").setData(result);
    }

    /**
     * 学生填报志愿
     *
     * @param expectation
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult addOneE(Expectation expectation, HttpServletRequest request, HttpServletResponse response) {
        //检查学生是否登录
        Student student = studentService.checkStudent(request,response);
        if(student == null){
            log.info("学生未登录");
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(student.getStudentId() == null){
            log.info("学生未登录");
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //判断登录的学生ID是否和当前志愿的ID一致
        if(!student.getStudentId().equals(expectation.getStudentId())){
            log.info("操作有误");
            return ResponseResult.FAILED("操作有误！");
        }
        Student studentById = studentDao.findStudentByStudentId(student.getStudentId());
        //判断该学生的志愿是否已经存在
        int trainingIdBystudentId = studentDao.findStudentByStudentId(expectation.getStudentId()).getTrainingId();
        Optional<Expectation> result = expectationDao.findById(expectation.getStudentId());
//        Expectation expectationById = result.isPresent()?result.get():null;
        if(result.isPresent()){
            log.info("志愿已存在");
            return ResponseResult.FAILED("请勿重复提交志愿!");
        }
        StudentProjectGrade spg = studentProjectGradeDao.findByStudentId(studentById.getStudentId());
        if(spg != null){
            return ResponseResult.FAILED("你已经在一个项目中，不能报志愿");
        }
        //判断expectation是否为空
        if(EhasNull(expectation)){
            log.info("志愿有空值");
            return ResponseResult.FAILED("输入志愿不合法！");
        }
        //判断expectation是否有重复
        if(!EhasRepeat(expectation)){
            log.info("志愿有重复");
            return ResponseResult.FAILED(("输入志愿不合法！"));
        }
        //判断志愿是否都在可以选择的范围内
        if(!EcanBeChoosen(expectation,studentById.getTrainingId())){
            log.info("志愿中有不属于这次暑期实训的项目");
            return ResponseResult.FAILED("志愿中有不属于这次暑期实训的项目!");
        }
        log.info("校验完成,开始存储");
        expectationDao.save(expectation);
        log.info("存储成功!");
        return ResponseResult.SUCCESS("添加志愿成功！");
    }

    /**
     * 进行志愿分配
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult allotE(int trainingId,HttpServletRequest request, HttpServletResponse response) {
        //get到教务ID
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(jiaowu == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(jiaowu.getJiaowuId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        String jiaowuId = jiaowu.getJiaowuId();
        log.info("教务  "+jiaowuId+"  正在分配实训  "+trainingId+"  的志愿");
        //检查教务是否有权操作
        Training trainingById = trainingDao.findByTrainingId(trainingId);
        if(!trainingById.getJiaowuId().equals(jiaowuId)){
            log.info("无权操作此实训");
            return ResponseResult.FAILED("教务无权操作");
        }
        //查出所有的志愿
        List<Map<String,Object>> result = expectationDao.listEByTraining(trainingId);
        long[][] data1 = new long[result.size()][7];
        log.info("总条目:  "+result.size());
        for(int i = 0; i < result.size(); i ++){
            data1[i][0] =Long.parseLong(result.get(i).get("studentId").toString().trim());
            data1[i][1] =Long.parseLong(result.get(i).get("e1").toString());
            data1[i][2] =Long.parseLong(result.get(i).get("e2").toString());
            data1[i][3] =Long.parseLong(result.get(i).get("e3").toString());
            data1[i][4] =Long.parseLong(result.get(i).get("e4").toString());
            data1[i][5] =Long.parseLong(result.get(i).get("e5").toString());
            data1[i][6] =0;
        }
        //查出可以报名的项目和最大人数
        List<Project> projects = projectDao.listProjectCanChoose(trainingId);
        int[][] projectDataByDb= new int[projects.size()][2];
        for(int j = 0;j < projects.size();j ++){
            projectDataByDb[j][0] = projects.get(j).getProjectId();
            Map<String,Object> temp = studentProjectGradeDao.SumNumOfProject(projectDataByDb[j][0]);
            int existNum =Integer.parseInt(temp.get("SumNum").toString());
            projectDataByDb[j][1] = projects.get(j).getProjectMaxNum() - existNum;
        }
        //项目数组转换成需要的格式
        int[][] projectData = new int[projects.size()][3];
        for(int j = 0;j<projects.size();j++){
            projectData[j][0] = projectDataByDb[j][1];
            projectData[j][1] = 0;
            projectData[j][2] = projects.get(j).getProjectId();
            log.info("项目  "+projects.get(j).getProjectId()+  "("+j+")"+ "  \t最大人数  "+projectDataByDb[j][1]);
        }
        //志愿数组转换成需要的格式
        int[][] stuData = new int[result.size()][6];
        for(int k = 0;k < result.size();k ++){//遍历学生
            for(int m = 0;m < 5;m  ++){//遍历5个志愿
                for(int n = 0;n < projects.size();n ++){//把志愿中的项目ID映射为从0开始
                    if( (int)data1[k][m+1] == projectData[n][2]){
                        stuData[k][m] = n;
                    }
                }
            }
            stuData[k][5] = -1;
            log.info("学生 "+result.get(k).get("studentId").toString()+"\t"+data1[k][1]+"("+stuData[k][0]+")\t"+data1[k][2]+"("+stuData[k][1]+")\t"+data1[k][3]+"("+stuData[k][2]+")\t"+data1[k][4]+"("+stuData[k][3]+")\t"+data1[k][5]+"("+stuData[k][4]+")"+"\t结果"+stuData[k][5]);

        }


        log.info("执行分配算法");
        int[][] stuDataResult = AllocationUtils.allocate1(stuData,projectData);
        log.info("结果");
        for(int i = 0;i < result.size();i ++){
            log.info(stuDataResult[i][0]+"\t"+stuDataResult[i][1]+"\t"+stuDataResult[i][2]+"\t"+stuDataResult[i][3]+"\t"+stuDataResult[i][4]+"\t最终"+stuDataResult[i][5]);
        }
        //输出projectData
        log.info("输出projectData");
        for(int i= 0;i < projects.size();i ++){
            log.info("第 "+i+" 行: "+projectData[i][0]+"\t"+projectData[i][1]+"\t"+projectData[i][2]);
        }
        //把结果转换为真正的项目ID
        for(int i = 0;i < result.size();i ++){
                        log.info("学生  "+data1[i][0]+"  的最终志愿为  "+projectData[stuDataResult[i][5]][2]);
                        data1[i][6] = projectData[stuDataResult[i][5]][2];
                        stuDataResult[i][5] = (int) data1[i][6];
        }
        log.info("映射后结果");
        for(int i = 0;i < result.size();i ++){
            log.info(stuDataResult[i][0]+"\t"+stuDataResult[i][1]+"\t"+stuDataResult[i][2]+"\t"+stuDataResult[i][3]+"\t"+stuDataResult[i][4]+"\t最终"+stuDataResult[i][5]);
        }
        log.info("开始存入数据库");
        List<Map<String,Object>> NoAllotResult = new ArrayList<>();
            for(int i = 0;i < result.size();i++){
                if(stuDataResult[i][5] != -1){
                    StudentProjectGrade spgTemp = new StudentProjectGrade();
                    spgTemp.setStudentId(result.get(i).get("studentId").toString());
                    spgTemp.setProjectId((int)data1[i][6]);
                    spgTemp.setGrades100Points(-1);
                    spgTemp.setGrades5Points(-1);
                    spgTemp.setIsleader(0);
                    spgTemp.setIsjointed(1);
                    spgTemp.setTrainingId(trainingId);
                    log.info("studentId  "+spgTemp.getStudentId()+"  projectId  "+spgTemp.getProjectId());
                    studentProjectGradeDao.save(spgTemp);
                }else {
                    log.info("学生  "+result.get(i).get("studentId").toString()+"  没有分配到志愿");
                    Map<String,Object> mapTemp = new HashMap<>();
                    mapTemp.put("studentId",result.get(i).get("studentId").toString());
                    NoAllotResult.add(mapTemp);
                }
            }
            log.info("分配完成!");
            log.info("");
            log.info("");
            log.info("");
            log.info("");
            return ResponseResult.SUCCESS("分配完成，共分配了  "+result.size()+"  条志愿").setData(data1);
    }


    /**
     * 判断志愿是否为空
     *
     * @param expectation
     * @return
     */
    private boolean EhasNull(Expectation expectation){
        if(expectation.getStudentId() == null){
            return true;
        }else{
            int e1 = expectation.getExpectation1();
            int e2 = expectation.getExpectation2();
            int e3 = expectation.getExpectation3();
            int e4 = expectation.getExpectation4();
            int e5 = expectation.getExpectation5();
            if(e1 == 0 || e2 == 0 || e3 == 0 || e4 == 0 || e5 == 0){
                return true;
            }else {
                return false;
            }
        }
    }

    /**
     * 判断志愿有无重复
     *
     * @param expectation
     * @return
     */
    private boolean EhasRepeat(Expectation expectation){
        int e1 = expectation.getExpectation1();
        int e2 = expectation.getExpectation2();
        int e3 = expectation.getExpectation3();
        int e4 = expectation.getExpectation4();
        int e5 = expectation.getExpectation5();
        int[] array = {e1,e2,e3,e4,e5};
        HashSet<Integer> hashSet = new HashSet<Integer>();
        for (int i = 0; i < array.length; i++) {
            hashSet.add(array[i]);
        }
        if (hashSet.size() == array.length) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断志愿是否是由教师创建并且是否属于学生的实训
     *
     * @param expectation
     * @param trainingId
     * @return
     */
    private boolean EcanBeChoosen(Expectation expectation , int trainingId){
        int e1 = expectation.getExpectation1();
        int e2 = expectation.getExpectation2();
        int e3 = expectation.getExpectation3();
        int e4 = expectation.getExpectation4();
        int e5 = expectation.getExpectation5();

        int[] array = {e1,e2,e3,e4,e5};
        for(int i = 0 ; i < 5 ; i ++){
            log.info("志愿  "+i+"  "+array[i]);
            Project project = projectDao.findByProjectId(array[i]);
            log.info(project.getProjectApplicantType()+"  "+project.getTrainingId());
            if(!project.getProjectApplicantType().equals("教师") || project.getTrainingId() != trainingId){
                return false;
            }
        }
        return true;
    }





}
