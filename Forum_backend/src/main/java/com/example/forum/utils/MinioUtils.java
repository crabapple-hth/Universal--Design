package com.example.forum.utils;

import io.minio.MinioClient;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MinioUtils {
    @Resource
    MinioClient client;

    public String upload(MultipartFile file){
        return null;
    }
}
