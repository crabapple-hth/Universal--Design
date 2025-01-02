package com.example.forum.Controller;


import com.example.forum.Entity.Dto.Topic;
import com.example.forum.Entity.RestBean;
import com.example.forum.Service.TopicService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/index")
public class Index {
    @Resource
    TopicService service;

    @GetMapping("/all-topics")
    public RestBean<Object> getTopics(){
        List<Topic> topicList=service.getTopics();
        return RestBean.success(topicList);
    }
}
