package com.example.demo2.services.impl;

import com.example.demo2.dao.AdminnoticeDao;
import com.example.demo2.pojo.Admin;
import com.example.demo2.pojo.Adminnotice;
import com.example.demo2.response.ResponseResult;
import com.example.demo2.services.AdminnoticeService;
import com.example.demo2.services.IAdminService;
import com.example.demo2.utils.Constants;
import com.example.demo2.utils.CookieUtils;
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
public class AdminnoticeServiceImpl implements AdminnoticeService {

    @Autowired
    private AdminnoticeDao adminnoticeDao;

    @Autowired
    private IAdminService adminService;


    /**
     * 发布一条公告
     *
     * @param adminnotice
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult addAdminnotice(Adminnotice adminnotice, HttpServletRequest request, HttpServletResponse response) {

        Admin admin = adminService.checkAdmin(request,response);
        if(admin == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //admin不为空，那么可以继续进行下一步操作，添加管理员公告
        String title = adminnotice.getAdminnoticeTitle();
        String content = adminnotice.getAdminnoticeContent();
        System.out.println("notice content ==> "+ content);
        System.out.println("notice title   ==> "+ title);
        //还要知道是哪个管理员发的公告,确认adminId
        String tokenKey = CookieUtils.getCookie(request, Constants.User.COOKIE_TOKE_KEY);
        if(tokenKey == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        adminnotice.setAdminId(admin.getAdminId());
        adminnoticeDao.save(adminnotice);
        return ResponseResult.SUCCESS("发布公告成功！");
    }

    /**
     * 查询所有公告（除了管理员ID信息）
     *
     * @param page
     * @return
     */
    @Override
    public ResponseResult findAllAdminnotice(int page) {
        log.info("正在查询所有管理员公告");
        Pageable pageable = PageRequest.of(page-1,Constants.Page.DEFAUT_PAGE_SIZE);
        Page<List<Map<String,Object>>> result = adminnoticeDao.findByPage(pageable);
        return ResponseResult.SUCCESS("分页查询管理员公告成功！").setData(result);
    }


    /**
     * 更新一条公告
     *
     * @param adminnotice
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult updateAdminnotice(Adminnotice adminnotice, HttpServletRequest request, HttpServletResponse response) {
        //检查是否登录
        Admin admin = adminService.checkAdmin(request,response);
        if(admin == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        Adminnotice noticeById = adminnoticeDao.findOneByAdminnoticeId(adminnotice.getAdminnoticeId());
        if(noticeById == null){
            return ResponseResult.FAILED("所要更新的公告不存在！");
        }
        //检查更换的字段
        BeanUtils.copyProperties(adminnotice, noticeById, getNullPropertyNames(adminnotice));

        //存入数据库
        adminnoticeDao.save(noticeById);

        //返回结果
        return ResponseResult.SUCCESS("更新成功");

    }

    /**
     * 删除一条公告
     *
     * @param adminnoticeId
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult deleteAdminnotice(Integer adminnoticeId, HttpServletRequest request, HttpServletResponse response) {
        //检查是否登录
        Admin admin = adminService.checkAdmin(request,response);
        if(admin == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        Adminnotice noticeById = adminnoticeDao.findOneByAdminnoticeId(adminnoticeId);
        if(noticeById == null){
            return ResponseResult.FAILED("所要删除的公告不存在！");
        }
        adminnoticeDao.deleteById(adminnoticeId);

        return ResponseResult.SUCCESS("删除成功");
    }




}
