package com.example.demo2.controller;

import com.example.demo2.dao.AdminnoticeDao;
import com.example.demo2.pojo.Admin;
import com.example.demo2.pojo.Adminnotice;
import com.example.demo2.response.ResponseResult;
import com.example.demo2.services.AdminnoticeService;
import com.example.demo2.services.IAdminService;
import com.example.demo2.utils.Constants;
import com.example.demo2.utils.CookieUtils;
import com.example.demo2.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.List;

@Transactional  //事务管理  添加
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {



//
//    @Autowired
//    private SystemlogDao systemlogDao;
//
//    @Autowired
//    RedisUtils redisUtils;
//
//    @GetMapping("/hello")
//    public ResponseResult helloworld(){
//        log.info("hello world！");//打印log
////        System.out.println("aaa");
//        ResponseResult responseResult = ResponseResult.SUCCESS().setData("hello world!");
//        //测试存入redis
////        redisUtils.set(Constants.User.KEY_CONTENT + "123456","resq",60 * 2);
////        System.out.println(redisUtils.get(Constants.User.KEY_CONTENT + "123456"));
////        System.out.println("success");
//        return responseResult;
//    }
//
//
//    @PostMapping("/testlogin")
//    public ResponseResult testLogin(){
//       return ResponseResult.SUCCESS("登录成功!");
//    }
//
//
//    @PostMapping("/systemlog")
//    public ResponseResult addLog(@RequestBody Systemlog systemlog){
//        systemlogDao.save(systemlog);
//        return ResponseResult.SUCCESS("操作成功");
//    }
//
//    @DeleteMapping("/systemlog/{logId}")
//    public ResponseResult deletesystemlog(@PathVariable("logId") Integer logId){
//
//        int deleteresult = systemlogDao.adminDeleteLogById(logId);
//        log.info("Delete Result == > "+deleteresult);
//        if(deleteresult > 0){
//            return ResponseResult.SUCCESS("删除成功");
//        }else {
//            return ResponseResult.FAILED("删除失败！ID不存在！");
//        }
//    }
//
//    @PutMapping("/systemlog/{logId}")
//    public ResponseResult updatesystemlog(@PathVariable("logId") Integer logId,@RequestBody Systemlog systemlog){
//        Systemlog dbSystemlog = systemlogDao.findOneByLogId(logId);
//        if(dbSystemlog == null){
//            return ResponseResult.FAILED("日志不存在！");
//        }
//        dbSystemlog.setLogContent(systemlog.getLogContent());
//        systemlogDao.save(dbSystemlog);
//        return ResponseResult.SUCCESS("修改成功！");
//    }
//
//    @GetMapping("/systemlog/{logId}")
//    public ResponseResult getSystemLogById(@PathVariable("logId") Integer logId){
//        Systemlog dbSystemlog = systemlogDao.findOneByLogId(logId);
//        if(dbSystemlog == null){
//            return ResponseResult.FAILED("日志不存在！");
//        }
//        return ResponseResult.SUCCESS("查询成功！").setData(dbSystemlog);
//
//    }
//
//    @GetMapping("/systemlog/page/{page}")
//    public ResponseResult listSystemLog(@PathVariable("page")int page){
//        if(page < 1){
//            page = 1;
//        }
//
//        Sort sort = Sort.by(Sort.Direction.DESC,"logCreateTime");
//        Pageable pageable = PageRequest.of(page -1, Constants.Page.DEFAUT_PAGE_SIZE,sort); //创建时间降序
//        Page<Systemlog> result = systemlogDao.findAll(pageable);
//        return ResponseResult.SUCCESS("分页查询成功!").setData(result);
//    }
//
//    //条件查询
//    @GetMapping("/systemlog/search/{page}")
//    public ResponseResult doSystemLogSearch(@RequestParam("keyword")String keyword,@PathVariable("page")int page){
//        Sort sort = Sort.by(Sort.Direction.DESC,"logCreateTime");
////        Pageable pageable = PageRequest.of(page -1, Constants.DEFAULT_SIZE,sort); //创建时间降序
//        List<Systemlog> all = systemlogDao.findAll(new Specification<Systemlog>() {
//            @Override
//            public Predicate toPredicate(Root<Systemlog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
//                Predicate predicate = cb.equal(root.get("logContent").as(String.class),keyword);
//                return predicate;
//            }
//        },sort);
//        if(all.size() == 0){
//            return ResponseResult.FAILED("数据不存在！结果为空！");
//        }
//        return ResponseResult.SUCCESS("查找成功！").setData(all);
//    }
//
//    //模糊分页查询
//    @GetMapping("/systemlog/likesearch/{page}")
//    public ResponseResult doSystemLogLikeSearch(@RequestParam("keyword")String keyword,@PathVariable("page")int page){
//        Sort sort = Sort.by(Sort.Direction.DESC,"logCreateTime");
//        Pageable pageable = PageRequest.of(page -1, Constants.Page.DEFAUT_PAGE_SIZE,sort); //创建时间降序
//        Page<Systemlog> result = systemlogDao.findAll(new Specification<Systemlog>() {
//            @Override
//            public Predicate toPredicate(Root<Systemlog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
//                Predicate predicate = cb.like(root.get("logContent").as(String.class),"%" + keyword + "%");
//                return predicate;
//            }
//        },pageable);
//        if(result.getTotalElements() == 0){//判断总条目
//            return ResponseResult.FAILED("数据不存在！结果为空！");
//        }
//        return ResponseResult.SUCCESS("查找成功！").setData(result);
//    }
//
//    //模糊联立分页查询
//    @GetMapping("/systemlog/likeandsearch/{page}")
//    public ResponseResult doSystemLogLikeAndSearch(@RequestParam("keyword")String keyword,@RequestParam("logId") int logId,@PathVariable("page")int page){
//        Sort sort = Sort.by(Sort.Direction.DESC,"logCreateTime");//创建时间降序
//        Pageable pageable = PageRequest.of(page -1, Constants.Page.DEFAUT_PAGE_SIZE,sort); //创建分页
//        Page<Systemlog> result = systemlogDao.findAll(new Specification<Systemlog>() {
//            @Override
//            public Predicate toPredicate(Root<Systemlog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
//                Predicate contentPre = cb.like(root.get("logContent").as(String.class),"%" + keyword + "%");
//                Predicate idPre = cb.equal(root.get("logId").as(Integer.class),logId);
//                Predicate and = cb.and(contentPre,idPre);
//                return and;
//            }
//        },pageable);
//        if(result.getTotalElements() == 0){//判断总条目
//            return ResponseResult.FAILED("数据不存在！结果为空！");
//        }
//        return ResponseResult.SUCCESS("查找成功！").setData(result);
//    }

    @Autowired
    private AdminnoticeDao adminnoticeDao;
    @Autowired
    private IAdminService iAdminService;
    @Autowired
    private AdminnoticeService adminnoticeService;


    //添加管理员公告
    @PostMapping("/adminnotice")
    public ResponseResult testNotice(@RequestBody Adminnotice adminnotice ,
                                     HttpServletRequest request ,
                                     HttpServletResponse response){
        Admin admin = iAdminService.checkAdmin(request,response);
        if(admin == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //admin不为空，那么可以继续进行下一步操作，添加管理员公告
        String title = adminnotice.getAdminnoticeTitle();
        String content = adminnotice.getAdminnoticeContent();
        System.out.println("notice content ==> "+ content);
        System.out.println("notice title   ==> "+ title);
        //还要知道是哪个管理员发的公告,确认adminId
        String tokenKey = CookieUtils.getCookie(request,Constants.User.COOKIE_TOKE_KEY);
        if(tokenKey == null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        adminnotice.setAdminId(admin.getAdminId());
        adminnoticeDao.save(adminnotice);
        return ResponseResult.SUCCESS("发布公告成功！");
    }


}
