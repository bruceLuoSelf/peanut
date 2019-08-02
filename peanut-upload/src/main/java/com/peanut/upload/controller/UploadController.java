package com.peanut.upload.controller;

import com.peanut.upload.service.IUploadService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ljn
 * @date 2019/8/2.
 */
@RestController
@RequestMapping("upload")
public class UploadController {

    @Autowired
    private IUploadService uploadService;

    @PostMapping("image")
    @ApiOperation(value = "上传图片")
    public ResponseEntity<String> uploadImage(@RequestParam("file")MultipartFile file) {
        return ResponseEntity.ok(uploadService.upload(file));
    }
}
