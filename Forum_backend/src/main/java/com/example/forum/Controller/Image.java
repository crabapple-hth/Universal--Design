package com.example.forum.Controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.forum.Entity.Dto.Account;
import com.example.forum.Entity.RestBean;
import com.example.forum.Mapper.UserMapper;
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

    @Resource
    UserMapper userMapper;

    @PostMapping("/topicImage")
    public RestBean<String> uploadImage(@RequestParam("file")MultipartFile file,
                                        @RequestAttribute("user_id") int userId) {
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

    @PostMapping("/commentImage")
    public RestBean<String> uploadCommentImage(@RequestParam("file") MultipartFile file,
                                               @RequestAttribute("user_id") int userId){
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

    @PostMapping("/avatar")
    public RestBean<String> uploadAvatarImage(@RequestParam("file") MultipartFile file,
                                               @RequestAttribute("user_id") int userId){
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
            UpdateWrapper<Account> updateWrapper=new UpdateWrapper<>();
            updateWrapper.eq("user_id",userId).set("avatar",file.getOriginalFilename());
            userMapper.update(updateWrapper);
            return RestBean.success(file.getOriginalFilename());
        } catch (Exception e) {
            System.out.println(e);
            return RestBean.failure(400,"上传出现了一些错误");
        }
    }

}
