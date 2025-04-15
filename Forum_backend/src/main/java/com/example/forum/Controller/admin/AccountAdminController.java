package com.example.forum.Controller.admin;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.forum.Entity.Dto.Account;
import com.example.forum.Entity.Dto.AccountInfo;
import com.example.forum.Entity.RestBean;
import com.example.forum.Entity.Vo.response.AccountVO;
import com.example.forum.Service.AccountInfoService;
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



    private void handleBanned(Account old, Account current) {
        String key = "banned:block"+ old.getUserid();
        if(!old.isBanned() && current.isBanned()) {
            template.opsForValue().set(key, "true", 72, TimeUnit.HOURS);
        } else if(old.isBanned() && !current.isBanned()) {
            template.delete(key);
        }
    }
}
