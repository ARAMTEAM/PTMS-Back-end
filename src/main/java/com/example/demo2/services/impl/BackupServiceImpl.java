package com.example.demo2.services.impl;

import com.example.demo2.pojo.Admin;
import com.example.demo2.response.ResponseResult;
import com.example.demo2.services.AdminnoticeService;
import com.example.demo2.services.BackupService;
import com.example.demo2.services.IAdminService;
import com.example.demo2.utils.BackupConstants;
import com.example.demo2.utils.FileUtil;
import com.example.demo2.utils.MySqlBackupRestoreUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BackupServiceImpl implements BackupService {

    @Autowired
    private IAdminService adminService;


    /**
     * 列出备份列表
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult queryBackup(HttpServletRequest request, HttpServletResponse response){
        log.info("查看所有备份列表");
        Admin admin = adminService.checkAdmin(request,response);
        if(admin == null || admin.getAdminId() == null){
            log.info("没有登陆");
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        List<Map<String, String>> backupRecords = new ArrayList<>();
        File restoreFolderFile = new File(BackupConstants.RESTORE_FOLDER);
        if(restoreFolderFile.exists()) {
            for(File file:restoreFolderFile.listFiles()) {
                Map<String, String> backup = new HashMap<>();
                backup.put("title", file.getName());
                backupRecords.add(backup);
            }
        }
        // 按时间戳排序，新备份在前面
        backupRecords.sort((o1, o2) -> o2.get("title").compareTo(o1.get("title")));
        return ResponseResult.SUCCESS("获取备份列表成功").setData(backupRecords);
    }

    /**
     * 备份数据
     *
     * @param name
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult backup(String name, HttpServletRequest request, HttpServletResponse response){
        log.info("开始进行数据库备份******************************");
        Admin admin = adminService.checkAdmin(request,response);
        if(admin == null || admin.getAdminId() == null){
            log.info("没有登陆");
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        String host = BackupConstants.HOST;
        String userName = BackupConstants.USER_NAME;
        String password = BackupConstants.PASSWORD;
        String database = BackupConstants.DATABASE;
        String backupFolderPath = BackupConstants.BACKUP_FOLDER + name + File.separator;
        String fileName = name;
        log.info("校验成功，开始进行备份");
        try {
            boolean success = MySqlBackupRestoreUtils.backup(host, userName, password, backupFolderPath, fileName, database);
            if(!success) {
                log.info("数据备份失败");
                return ResponseResult.FAILED("备份失败!");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        log.info("备份成功！文件存储在:  "+backupFolderPath+fileName);
        return ResponseResult.SUCCESS("备份成功！文件存储在:  "+backupFolderPath+fileName+".sql");
    }

    /**
     * 恢复到数据库
     *
     * @param name
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult restore(String name,HttpServletRequest request, HttpServletResponse response){
        log.info("开始进行数据库还原******************************");
        Admin admin = adminService.checkAdmin(request,response);
        if(admin == null || admin.getAdminId() == null){
            log.info("没有登陆");
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        String host = "localhost";
        String userName = BackupConstants.USER_NAME;
        String password = BackupConstants.PASSWORD;
        String database = BackupConstants.DATABASE;
        String restoreFilePath = BackupConstants.RESTORE_FOLDER + name;
        log.info("校验成功，开始进行还原");
        try {
            MySqlBackupRestoreUtils.restore(restoreFilePath, host, userName, password, database);
            log.info("还原成功，数据库已经还原为: "+name);
            return ResponseResult.SUCCESS("还原成功，数据库已经还原为: "+name);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("恢复失败");
            return ResponseResult.FAILED("恢复失败");
        }
    }

    /**
     * 删除备份
     *
     * @param name
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult delBackup(String name,HttpServletRequest request, HttpServletResponse response){
        log.info("开始进行备份删除******************************");
        Admin admin = adminService.checkAdmin(request,response);
        if(admin == null || admin.getAdminId() == null){
            log.info("没有登陆");
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        String restoreFilePath = BackupConstants.BACKUP_FOLDER + name;
        try {
            FileUtil.deleteFile(new File(restoreFilePath));
            log.info("删除备份  "+name+"  成功");
            return ResponseResult.SUCCESS("删除备份  "+name+"  成功");
        } catch (Exception e) {
            System.out.println(e);
            log.info("删除失败");
            return ResponseResult.FAILED("删除失败！");
        }
    }
}