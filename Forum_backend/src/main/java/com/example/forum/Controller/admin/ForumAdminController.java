package com.example.forum.Controller.admin;


import com.example.forum.Entity.Dto.Announce;
import com.example.forum.Entity.RestBean;
import com.example.forum.Service.ForumService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/forum")
public class ForumAdminController {
    @Resource
    ForumService service;

    @GetMapping("/announce")
    public RestBean<Object> getAnnounce(){
        List<Announce> list=service.getAnnounce();
        return RestBean.success(list);
    }
}
