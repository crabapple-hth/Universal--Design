package com.example.forum.Controller;

import com.example.forum.Entity.RestBean;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/image")
public class Image {
    @Resource
    MinioClient client;

    @PostMapping("/topicImage")
    public RestBean<String> uploadImage(@RequestParam("file")MultipartFile file,
                                        @RequestAttribute("user_id") int userId) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (file.getSize() > 1024 * 1024 * 5)
            return RestBean.failure(400, "图片过大");
        try {
            InputStream inputStream = file.getInputStream();
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket("test")
                    .object(file.getOriginalFilename())
                    .stream(inputStream, inputStream.available(), -1)
                    .contentType(file.getContentType())
                    .build();
            client.putObject(args);
            return RestBean.success(file.getOriginalFilename());
        } catch (Exception e) {
            System.out.println(e);
            return RestBean.failure(400,"上传出现了一些错误");
        }

    }
}
