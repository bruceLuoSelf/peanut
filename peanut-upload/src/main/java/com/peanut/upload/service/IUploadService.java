package com.peanut.upload.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author ljn
 * @date 2019/8/2.
 */
public interface IUploadService {

    String upload(MultipartFile file);
}
