package com.example.demo2.controller.fileResource;

import com.example.demo2.services.impl.FileResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
public class FileUploadApi {


    @Autowired
    private FileResourceService filePathService;


    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) {
        return filePathService.Upload(file);
    }

    @PreAuthorize("@permission.CheckUser() >= 3")
    @GetMapping("/download")
    @ResponseBody
    public String downloadFile(@RequestParam("fileId") int fileId , HttpServletResponse res) throws IOException {
        return filePathService.Download(res,fileId);
    }
}
