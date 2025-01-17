package com.example.forum.Controller;

import com.example.forum.Entity.RestBean;
import com.example.forum.Entity.Vo.request.TopicCreatVO;
import com.example.forum.Service.TopicService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class TopicController {

    @Resource
    TopicService service;

    @PostMapping("/creat-topic")
    public RestBean<Void> creatTopic(@RequestAttribute("user_id") int userId,
                                     @Valid @RequestBody TopicCreatVO vo){
        return service.creatTopic(userId,vo)==null ? RestBean.success() : RestBean.failure(400,"发帖错误");
    }
}
