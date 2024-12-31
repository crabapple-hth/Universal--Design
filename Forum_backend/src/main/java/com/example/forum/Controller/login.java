package com.example.forum.Controller;

import com.example.forum.Entity.RestBean;
import com.example.forum.Entity.Vo.request.RegisterVo;
import com.example.forum.Service.UserService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class login {

    @Resource
    UserService service;


    @GetMapping("/getCode")
    public RestBean<Object> GetCode(@Valid RegisterVo registerVo){
        service.getVerifyCode(registerVo);
        return RestBean.success();
    }

    @PostMapping("/register")
    public RestBean<Object> Register(@Valid RegisterVo registerVo){
        service.RegisterUserByEmail(registerVo);
        return RestBean.success();
    }
}
