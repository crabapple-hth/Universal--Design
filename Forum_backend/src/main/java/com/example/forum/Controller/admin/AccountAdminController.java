package com.example.forum.Controller.admin;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.forum.Entity.Dto.Account;
import com.example.forum.Entity.Dto.AccountInfo;
import com.example.forum.Entity.Dto.Topic;
import com.example.forum.Entity.RestBean;
import com.example.forum.Entity.Vo.response.AccountVO;
import com.example.forum.Entity.Vo.response.TopicDetailsVO;
import com.example.forum.Service.AccountInfoService;
import com.example.forum.Service.TopicService;
import com.example.forum.Service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/admin/user")
public class AccountAdminController {
    @Resource
    UserService service;

    @Resource
    TopicService topicService;


    @Resource
    AccountInfoService detailsService;


    @Resource
    StringRedisTemplate template;


    @GetMapping("/list")
    public RestBean<JSONObject> accountList(@RequestParam int page,
                                            @RequestParam int size){
        JSONObject object=new JSONObject();
        List<AccountVO> list = service.page(Page.of(page,size))
                .getRecords()
                .stream()
                .map(a->a.asViewObject(AccountVO.class))
                .toList();
        object.put("total",service.count());
        object.put("list",list);
        return RestBean.success(object);
    }

    @GetMapping("/detail")
    public RestBean<JSONObject> accountDetail(@RequestParam int id) {
        JSONObject object = new JSONObject();
        object.put("detail", detailsService.getAccountInfoById(id));
        return RestBean.success(object);
    }

    @PostMapping("/save")
    public RestBean<Void> saveAccount(@RequestBody JSONObject object) {
        int id = object.getInteger("userid");
        Account account = service.GetUserById(id);
        Account save = object.toJavaObject(Account.class);
        handleBanned(account,save);
        BeanUtils.copyProperties(save, account, "password", "creatTime");
        service.saveOrUpdate(account);
        AccountInfo details = detailsService.getAccountInfoById(id);
        AccountInfo saveDetails = object.getJSONObject("detail").toJavaObject(AccountInfo.class);
        BeanUtils.copyProperties(saveDetails, details);
        detailsService.saveOrUpdate(details);
        return RestBean.success();
    }

    @GetMapping("/topicList")
    public RestBean<JSONObject> topicList(@RequestParam int page,
                                          @RequestParam int size){
        JSONObject object=new JSONObject();
        // 创建Mybatis-Plus的分页对象
        Page<TopicDetailsVO> topicPage = new Page<>(page, size);
        // 调用服务层的分页查询帖子列表方法
        List<TopicDetailsVO> list = topicService.topicList(topicPage)
                .getRecords();
        // 将总记录数放入JSONObject中
        object.put("total", topicService.count()); // 假设您有一个查询帖子总数的方法
        // 将帖子列表放入JSONObject中
        object.put("list",list);
        // 返回成功结果和JSONObject数据
        return RestBean.success(object);
    }

    @GetMapping("delete")
    public RestBean<Void> delTopic(int tid){
        topicService.delTopic(tid);
        return RestBean.success();
    }

    @GetMapping("setTop")
    public RestBean<Void> setTop(int tid){
        Topic topic = topicService.getById(tid);
        if (topic==null)
            return RestBean.failure(401,"未找到对于帖子，请重试");
        topic.setTop(!topic.getTop());
        topicService.saveOrUpdate(topic);
        return RestBean.success();
    }



    private void handleBanned(Account old, Account current) {
        String key = "banned:block"+ old.getUserid();
        if(!old.isBanned() && current.isBanned()) {
            template.opsForValue().set(key, "true", 72, TimeUnit.HOURS);
        } else if(old.isBanned() && !current.isBanned()) {
            template.delete(key);
        }
    }
}
