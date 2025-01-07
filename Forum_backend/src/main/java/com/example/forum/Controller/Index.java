package com.example.forum.Controller;


import com.example.forum.Entity.Dto.Topic;
import com.example.forum.Entity.RestBean;
import com.example.forum.Service.TopicService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/index")
public class Index {
    @Resource
    TopicService service;

    @GetMapping("/all-topics")
    public RestBean<Object> getTopics(@RequestParam int current){
        HashMap<String,List> topicInfo=service.getTopics(current);
        return topicInfo!=null ? RestBean.success(topicInfo) : RestBean.failure(400,"请求帖子失败");
    }

    @GetMapping("/user-like-collect")
    public RestBean<Object> getUserLikeCollect(@RequestParam int topicId
            ,@RequestAttribute("user_id") int userid){
        List<Boolean> list=service.getTopicLikeCollect(topicId,userid);
        return list!=null ?  RestBean.success(list) :RestBean.failure(400,"点赞、收藏发生了错误");
    }

    @GetMapping("/like")
    public RestBean<Void> changeLike(@RequestParam int topicId,
                                     @RequestAttribute("user_id") int userid,
                                     @RequestParam int like){
        service.changeLike(topicId,userid,like);
        return RestBean.success();
    }

    @GetMapping("/collect")
    public RestBean<Void> changeCollect(@RequestParam int topicId,
                                     @RequestAttribute("user_id") int userid,
                                     @RequestParam int collect){
        service.changeCollect(topicId,userid,collect);
        return RestBean.success();
    }
}
