package com.example.demo2.services.impl;

import com.example.demo2.dao.SlogsDao;
import com.example.demo2.pojo.Admin;
import com.example.demo2.pojo.Slogs;
import com.example.demo2.response.ResponseResult;
import com.example.demo2.services.IAdminService;
import com.example.demo2.services.SlogsService;
import com.example.demo2.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

@Service
@Slf4j
@Transactional
public class SlogsServiceImpl implements SlogsService {

    @Autowired
    private IAdminService adminService;
    @Autowired
    private SlogsDao slogsDao;

    /**
     * 查询
     *
     * @param page
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult listAllLogs(int page, HttpServletRequest request, HttpServletResponse response) {
        Admin admin = adminService.checkAdmin(request,response);
        if(admin == null || admin.getAdminId() == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        log.info("管理员查看全部日志");
        Sort sort = Sort.by(Sort.Direction.DESC,"time");
        Pageable pageable = PageRequest.of(page -1, Constants.Page.DEFAUT_PAGE_SIZE,sort);
        return ResponseResult.SUCCESS("查询成功").setData(slogsDao.findAll(pageable));
    }

    @Override
    public void savelog(String userType, String userId, String operation, String ip) {
        Slogs slogs = new Slogs();
        slogs.setUserType(userType);
        slogs.setUserId(userId);
        slogs.setOperation(operation);
        slogs.setIp(ip);
        slogsDao.save(slogs);
    }


}
