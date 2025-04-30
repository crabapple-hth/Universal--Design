package com.example.forum.Controller;


import com.example.forum.Entity.Dto.Account;
import com.example.forum.Entity.Dto.Topic;
import com.example.forum.Entity.Dto.UserCollects;
import com.example.forum.Entity.RestBean;
import com.example.forum.Entity.Vo.request.AccountInfoVO;
import com.example.forum.Entity.Vo.response.AccountVO;
import com.example.forum.Service.ServiceImpl.AccountInfoServiceImpl;
import com.example.forum.Service.ServiceImpl.TopicServiceImpl;
import com.example.forum.Service.ServiceImpl.UserServiceImpl;
import com.example.forum.Service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account/info")
public class AccountInfo {
    @Resource
    TopicServiceImpl service;

    @Resource
    UserServiceImpl userService;

    @Resource
    AccountInfoServiceImpl accountInfoService;

    @GetMapping("/details")
    public RestBean<Object> getAccount(@RequestAttribute("user_id") int userid){
        Account account=userService.GetUserById(userid);
        AccountVO accountVO=new AccountVO();
        BeanUtils.copyProperties(account,accountVO);
        return RestBean.success(accountVO);
    }

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

    @PostMapping("/update")
    public RestBean<Void> updateInfo(@RequestAttribute("user_id") int userId,
                                     @RequestBody AccountInfoVO vo){

        return  accountInfoService.updateUserInfo(userId,vo) ?
                RestBean.success() :
                RestBean.failure(400,"修改信息错误");
    }

    @GetMapping("/getInfo")
    public RestBean<Object> getInfo(@RequestAttribute("user_id") int userId){
        return RestBean.success(accountInfoService.getAccountInfoById(userId));
    }

}
