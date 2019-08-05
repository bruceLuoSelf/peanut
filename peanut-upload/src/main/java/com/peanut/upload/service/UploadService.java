package com.peanut.upload.service;

import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.peanut.enums.ExceptionEnum;
import com.peanut.exception.PeanutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ljn
 * @date 2019/8/2.
 */
@Service

public class UploadService implements IUploadService {

    private static final Logger log = LoggerFactory.getLogger(UploadService.class);

    private static final List<String> ALLOW_TYPES = new ArrayList<String>(Arrays.asList("image/jpeg", "image/png", "image/bmp"));

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Override
    public String upload(MultipartFile file) {
        try {
            // 校验文件类型
            String contentType = file.getContentType();
            if (!ALLOW_TYPES.contains(contentType)) {
                throw new PeanutException(ExceptionEnum.INVALID_FILE_TYPE);
            }
            // 校验文件内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new PeanutException(ExceptionEnum.INVALID_FILE_TYPE);
            }
//            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
//            StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);
//            return "http://image.peanut.com/" + storePath.getFullPath();

            // 目标路径
            File dest = new File("E:/gitRepository/upload/" + file.getOriginalFilename());
            // 保存文件到本地
            file.transferTo(dest);
            // 返回文件路径
            return "http://image.peanut.com/" + file.getOriginalFilename();
        } catch (IOException e) {
            log.info("上传文件失败" , e);
            throw new PeanutException(ExceptionEnum.UPLOAD_FILE_FAIL);
        }

    }
}
