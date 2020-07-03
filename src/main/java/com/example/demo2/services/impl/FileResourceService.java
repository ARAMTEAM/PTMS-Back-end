package com.example.demo2.services.impl;


import java.io.IOException;

import com.example.demo2.dao.FileDao;
import com.example.demo2.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo2.pojo.FileResource;

import javax.servlet.http.HttpServletResponse;


@Slf4j
@Service
public class FileResourceService {



    @Autowired
    private FileDao filePathRepository;

    public String Upload(@RequestParam("file") MultipartFile file) {
        if(!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String path ="D://Files/";
            //此路径可以自己定义，这样设置的话可以在自创的target/classes/static目录下保存，
            //在前端可以直接通过URL方式进行访问

            try {
                FileUtil.fileupload(file.getBytes(), path, fileName);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            FileResource biaopath = new FileResource();
            biaopath.setFilename(fileName);
            biaopath.setPath(path+fileName);
            filePathRepository.save(biaopath);
        }
        return "success upload";
    }

    public String Download(HttpServletResponse res, int fileId) throws IOException {

        FileResource fr = filePathRepository.findById(fileId).get();
        String fileName =fr.getFilename();
        res.setHeader("content-type", "application/octet-stream");
        res.setContentType("application/octet-stream");
        res.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        FileUtil.filedownload(res.getOutputStream(),fr.getPath());
        return "succes dowload";

    }
}