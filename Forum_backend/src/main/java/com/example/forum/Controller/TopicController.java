package com.example.forum.Controller;

import com.example.forum.Entity.Dto.Account;
import com.example.forum.Entity.Dto.Topic;
import com.example.forum.Entity.Dto.TopicType;
import com.example.forum.Entity.RestBean;
import com.example.forum.Entity.Vo.request.CommentCreatVO;
import com.example.forum.Entity.Vo.request.TopicCreatVO;
import com.example.forum.Entity.Vo.response.CommentWithUser;
import com.example.forum.Entity.Vo.response.TopicDetails;
import com.example.forum.Service.TopicService;
import com.example.forum.Service.UserService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class TopicController {

    @Resource
    TopicService service;

    @Resource
    UserService userService;

    @PostMapping("/creat-topic")
    public RestBean<Void> creatTopic(@RequestAttribute("user_id") int userId,
                                     @Valid @RequestBody TopicCreatVO vo){
        Account account=userService.GetUserById(userId);
        if (account.isMute()){
            return RestBean.forbidden("您已被禁言，无法发表帖子");
        }
        return service.creatTopic(userId,vo)==null ? RestBean.success() : RestBean.failure(400,"发帖错误");
    }

    @PostMapping("/creat-commend")
    public RestBean<Void> creatCommend(@RequestAttribute("user_id") int userId,
                                       @Valid @RequestBody CommentCreatVO vo){
        Account account=userService.GetUserById(userId);
        if (account.isMute()){
            return RestBean.forbidden("您已被禁言，无法发表评论");
        }
        return service.creatComment(userId,vo)==null ? RestBean.success() : RestBean.failure(400,"发帖错误");
    }

    @GetMapping("/getTopicDetails")
    public RestBean<Object> getTopicDetails(@RequestParam int topicId){
        TopicDetails topicDetails =service.getTopicById(topicId);
        return topicDetails!=null?RestBean.success(topicDetails):RestBean.failure(400,"请求帖子内容错误");
    }

    @GetMapping("/getComments")
    public RestBean<Object> getComments(@RequestParam int tid){
        List<CommentWithUser> comments= service.getTopLevelCommentsWithReplies(tid);
        return comments!=null? RestBean.success(comments):RestBean.failure(400,"请求评论出现问题");
    }

    @GetMapping("/getTypes")
    public RestBean<List<TopicType>> getTypes(){
        return RestBean.success(service.getTypeList());
    }
}
