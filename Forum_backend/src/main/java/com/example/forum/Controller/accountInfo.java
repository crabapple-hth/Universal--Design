package com.example.forum.Controller;


import com.example.forum.Entity.Dto.Topic;
import com.example.forum.Entity.Dto.UserCollects;
import com.example.forum.Entity.RestBean;
import com.example.forum.Service.ServiceImpl.TopicServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/account/info")
public class accountInfo {
    @Resource
    TopicServiceImpl service;

    @GetMapping("/collectList")
    public RestBean<Object> getCollectList(@RequestAttribute("user_id") int userid){
        List<UserCollects> collects = service.getCollects(userid);
        return collects!=null ? RestBean.success(collects) : RestBean.failure(400,"无collect");
    }

    @GetMapping("/likeList")
    public RestBean<Object> getLikeList(@RequestAttribute("user_id") int userid){
        List<UserCollects> likes = service.getLikes(userid);
        return RestBean.success(likes);
    }

    @GetMapping("/myTopics")
    public RestBean<Object> getMyTopics(@RequestAttribute("user_id") int userid){
        List<Topic> myTopics = service.getMyTopics(userid);
        return RestBean.success(myTopics);
    }

}
