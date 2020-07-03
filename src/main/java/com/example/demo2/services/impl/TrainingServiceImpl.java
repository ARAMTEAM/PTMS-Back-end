package com.example.demo2.services.impl;

import com.example.demo2.dao.TrainingDao;
import com.example.demo2.pojo.Jiaowu;
import com.example.demo2.pojo.Training;
import com.example.demo2.response.ResponseResult;
import com.example.demo2.services.JiaowuService;
import com.example.demo2.services.TrainingService;
import com.example.demo2.utils.Constants;
import lombok.extern.slf4j.Slf4j;
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
import java.util.List;
import java.util.Map;

import static com.example.demo2.utils.UpdateUtil.getNullPropertyNames;

@Service
@Slf4j
@Transactional
public class TrainingServiceImpl implements TrainingService {

    @Autowired
    private TrainingDao trainingDao;

    @Autowired
    private JiaowuService jiaowuService;

    /**
     * 根据教务ID查出他名下所有的实训
     *
     * @param page
     * @return
     */
    @Override
    public ResponseResult listAllTrainingByJiaowu(Integer page,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) {
        //检查教务是否登录，需要教务权限，并获取jiaowuId
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(jiaowu == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(jiaowu.getJiaowuId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(page < 1){
            page = 1;
        }
        String jiaowuId = jiaowu.getJiaowuId();
        log.info("当前登录的教务ID为    "+jiaowuId);
        Sort sort = Sort.by(Sort.Direction.ASC,"training_id");
        Pageable pageable = PageRequest.of(page -1, Constants.Page.DEFAUT_PAGE_SIZE,sort);
//        Page<Training> all = trainingDao.listAllTrainingByJiaowuId(jiaowuId,pageable);
        Page<List<Map<String,Object>>> all = trainingDao.findByJiaowuId(jiaowuId,pageable);
        return ResponseResult.SUCCESS("查询成功").setData(all);
    }

    /**
     * 不分页列出教务的实训
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult listAllTrainingByJiaowuNoPage(HttpServletRequest request, HttpServletResponse response) {
        //检查教务是否登录，需要教务权限，并获取jiaowuId
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(jiaowu == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(jiaowu.getJiaowuId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        String jiaowuId = jiaowu.getJiaowuId();
        log.info("当前登录的教务ID为    "+jiaowuId);
        Sort sort = Sort.by(Sort.Direction.ASC,"training_id");
        List<Map<String,Object>> all = trainingDao.findByJiaowuIdNoPage(jiaowuId);
        return ResponseResult.SUCCESS("查询成功").setData(all);
    }


    /**
     * 通过实训ID查询单个实训的信息
     *
     * @param trainingID
     * @return
     */
    @Override
    public ResponseResult listOneTraining(Integer trainingID) {
        List<Map<String,Object>> training= trainingDao.findjiaowuname(trainingID);
        return ResponseResult.SUCCESS("查询单个实训信息成功！").setData(training);
    }

    /**
     * 增加实训，需要教务登录
     *
     * @param training
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult addOneTraining(Training training, HttpServletRequest request, HttpServletResponse response) {
        //检查教务是否登录，需要教务权限，并获取jiaowuId
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(jiaowu == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(jiaowu.getJiaowuId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        String jiaowuId = jiaowu.getJiaowuId();

        log.info("登录校验通过，开始增加实训****************");
        training.setJiaowuId(jiaowuId);
        trainingDao.save(training);
        return ResponseResult.SUCCESS("添加实训成功！").setData(training);
    }

    /**
     * 更新实训，需要教务登录，并且只能修改属于自己的实训
     *
     * @param training
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult updateTraining(Training training, HttpServletRequest request, HttpServletResponse response) {
        //检查教务是否登录，需要教务权限，并获取jiaowuId
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(jiaowu == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(jiaowu.getJiaowuId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("教务  "+jiaowu.getJiaowuId()+"  正在更新实训  "+training.getTrainingId());
        String jiaowuId = jiaowu.getJiaowuId();
        Training trainingById = trainingDao.findByTrainingId(training.getTrainingId());
        trainingById.setJiaowuId(jiaowuId);
        //检查更新的字段
        BeanUtils.copyProperties(training, trainingById, getNullPropertyNames(training));
        //存入数据库
        trainingDao.save(trainingById);
        //返回结果
        return ResponseResult.SUCCESS("更新实训信息成功！");
    }

    /**
     * 删除实训，需要教务登录，并且只能删除属于自己的实训
     *
     * @param trainingId
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult deleteTraining(Integer trainingId,
                                         HttpServletRequest request,
                                         HttpServletResponse response) {
        //检查教务是否登录，需要教务权限，并获取jiaowuId
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(jiaowu == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if(jiaowu.getJiaowuId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        String jiaowuId = jiaowu.getJiaowuId();
        Training trainingById = trainingDao.findByTrainingId(trainingId);
        //检查要删除的实训是否存在
        if(trainingById == null){
            return ResponseResult.FAILED("要删除的实训不存在!");
        }
        //检查教务ID和要更新前实训的教务ID是否一致
        if(!trainingById.getJiaowuId().equals(jiaowuId)){
            return ResponseResult.FAILED("操作有误!");
        }
        int deletenum = trainingDao.deleteTrainingByTrainingId(trainingId);
        log.info("删除实训成功，共删除的记录条数为：== > "+ deletenum);
        return ResponseResult.SUCCESS("删除成功！").setData(deletenum);
    }



}
