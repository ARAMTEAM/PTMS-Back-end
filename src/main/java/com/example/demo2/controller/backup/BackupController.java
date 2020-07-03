package com.example.demo2.controller.backup;

import com.example.demo2.response.ResponseResult;
import com.example.demo2.services.BackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("Database")
public class BackupController {

    @Autowired
    private BackupService backupService;

    /**
     * 获取备份列表
     *
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 1")
    @GetMapping(value = "/backup")
    public ResponseResult getBackup(HttpServletRequest request, HttpServletResponse response){
        return backupService.queryBackup(request,response);
    }

    /**
     * 增加备份
     *
     * @param name
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 1")
    @PostMapping("/backup/{name}")
    public ResponseEntity backup(@PathVariable("name") String name,
                                 HttpServletRequest request,
                                 HttpServletResponse response){
        backupService.backup(name,request,response);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * 恢复备份
     *
     * @param name
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@permission.CheckUser() == 1")
    @PutMapping("/backup/{name}")
    public ResponseEntity restore(@PathVariable("name") String name,
                                  HttpServletRequest request,
                                  HttpServletResponse response){
        backupService.restore(name,request,response);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**BackupConstants
     * 删除备份
     *
     * @param name
     * @param request
     * @param response
     * @return
     */
    @DeleteMapping("/backup/{name}")
    public ResponseResult delBackup(@PathVariable("name") String name,
                                    HttpServletRequest request,
                                    HttpServletResponse response){
        return backupService.delBackup(name,request,response);

    }
}