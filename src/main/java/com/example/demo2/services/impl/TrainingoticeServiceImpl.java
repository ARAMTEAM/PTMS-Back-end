package com.example.demo2.services.impl;

import com.example.demo2.dao.TrainingDao;
import com.example.demo2.dao.TrainingnoticeDao;
import com.example.demo2.pojo.Jiaowu;
import com.example.demo2.pojo.Project;
import com.example.demo2.pojo.Training;
import com.example.demo2.pojo.Trainingnotice;
import com.example.demo2.response.ResponseResult;
import com.example.demo2.services.JiaowuService;
import com.example.demo2.services.TrainingnoticeService;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.demo2.utils.UpdateUtil.getNullPropertyNames;

@Service
@Slf4j
@Transactional
public class TrainingoticeServiceImpl implements TrainingnoticeService {

    @Autowired
    private TrainingnoticeDao trainingnoticeDao;
    @Autowired
    private JiaowuService jiaowuService;
    @Autowired
    private TrainingDao trainingDao;

    /**
     * 根据实训ID分页列出所有的公告
     *
     * @param trainingId
     * @param page
     * @return
     */
    @Override
    public ResponseResult listByJiaowuId(String trainingId, int page) {
        Sort sort = Sort.by(Sort.Direction.DESC,"trainingnoticeUpdateTime");
        Pageable pageable = PageRequest.of(page -1, Constants.Page.DEFAUT_PAGE_SIZE,sort);
        Page<List<Map<String,Object>>> result =  trainingnoticeDao.listByTrainingId(trainingId,pageable);
        return ResponseResult.SUCCESS("查询成功！").setData(result);
    }

    /**
     * 教务发布实训公告
     *
     * @param trainingnotice
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult addNotice(Trainingnotice trainingnotice, HttpServletRequest request, HttpServletResponse response) {
        //检验教务权限
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(jiaowu == null || jiaowu.getJiaowuId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("教务  "+jiaowu.getJiaowuId()+"  正在添加公告");
        //检查教务是否有权限操作
        Training training = trainingDao.findByTrainingId(trainingnotice.getTrainingId());
        if(training == null){
            log.info("实训不存在");
            return ResponseResult.FAILED("实训不存在，请检查ID");
        }
        //检查实训是不是归自己管
        if(!training.getJiaowuId().equals(jiaowu.getJiaowuId())){
            log.info("不能向不属于自己的实训添加公告");
            return ResponseResult.FAILED("不能向不属于自己的实训添加公告");
        }
        if(trainingnotice.getTrainingnoticeTitle() == null || trainingnotice.getTrainingnoticeContent() == null){
            log.info("内容有空值");
            return ResponseResult.FAILED("标题和内容不能为空!");
        }
        //设置时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        trainingnotice.setTrainingnoticeCreateTime(new Date());
        trainingnotice.setTrainingnoticeUpdateTime(new Date());
        trainingnoticeDao.save(trainingnotice);
        log.info("添加成功");
        return ResponseResult.SUCCESS("添加成功");
    }

    /**
     * 更新实训公告
     *
     * @param trainingnotice
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult updateNotice(Trainingnotice trainingnotice, HttpServletRequest request, HttpServletResponse response) {
        //检验教务权限
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(jiaowu == null || jiaowu.getJiaowuId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("教务  "+jiaowu.getJiaowuId()+"  正在更新公告");
        //先查出来旧的公告
        Optional<Trainingnotice> result = trainingnoticeDao.findById(trainingnotice.getTrainingId());
        if(!result.isPresent()){
            log.info("公告不存在");
            return ResponseResult.FAILED("要更新的公告不存在");
        }
        Trainingnotice trainingnoticeById = trainingnoticeDao.findById(trainingnotice.getTrainingnoticeId()).get();
        //检查教务是否有权限操作
        Training training = trainingDao.findByTrainingId(trainingnotice.getTrainingId());
        if(training == null){
            log.info("实训不存在");
            return ResponseResult.FAILED("实训不存在，请检查ID");
        }
        if(!training.getJiaowuId().equals(jiaowu.getJiaowuId())){
            log.info("无权操作");
            return ResponseResult.FAILED("无权操作此公告!");
        }
        if(trainingnotice.getTrainingId() != trainingnoticeById.getTrainingId()){
            log.info("不能更改公告的实训ID");
            return ResponseResult.FAILED("不能更改公告的实训ID");
        }
        //检查更新的字段
        BeanUtils.copyProperties(trainingnotice, trainingnoticeById, getNullPropertyNames(trainingnotice));
        //设置时间
        trainingnoticeById.setTrainingnoticeUpdateTime(new Date());
        trainingnoticeDao.save(trainingnoticeById);
        log.info("更新成功");
        return ResponseResult.SUCCESS("更新成功");
    }

    /**
     * 教务删除公告
     *
     * @param trainingnoticeId
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult deleteNotice(int trainingnoticeId, HttpServletRequest request, HttpServletResponse response) {
        //检验教务权限
        Jiaowu jiaowu = jiaowuService.checkJiaowu(request,response);
        if(jiaowu == null || jiaowu.getJiaowuId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("教务  "+jiaowu.getJiaowuId()+"  正在删除公告  "+trainingnoticeId);
        //先查出来旧的公告
        Optional<Trainingnotice> result = trainingnoticeDao.findById(trainingnoticeId);
        if(!result.isPresent()){
            log.info("公告不存在");
            return ResponseResult.FAILED("要删除的公告不存在");
        }
        Trainingnotice trainingnoticeById = result.get();
        //检查是否是这个教务发的公告
        Training training = trainingDao.findByTrainingId(trainingnoticeById.getTrainingId());
        if(!training.getJiaowuId().equals(jiaowu.getJiaowuId())){
            log.info("无权操作");
            return ResponseResult.FAILED("无权操作此公告!");
        }
        trainingnoticeDao.deleteById(trainingnoticeId);
        log.info("删除成功");
        return ResponseResult.SUCCESS("删除成功");
    }


}
