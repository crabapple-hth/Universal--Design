package com.example.forum.Controller;

import com.example.forum.Entity.RestBean;
import com.example.forum.Entity.Vo.request.RegisterVo;
import com.example.forum.Entity.Vo.request.ResetVO;
import com.example.forum.Service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class login {

    @Resource
    UserService service;


    @GetMapping("/getCode")
    public RestBean<Void> GetCode(@RequestParam @Email String email,
                                    @RequestParam int type,
                                    HttpServletRequest request){
        String message=service.getVerifyCode(email,type);
        return message==null ?  RestBean.success() : RestBean.failure(400,message);
    }

    @PostMapping("/register")
    public RestBean<Object> Register(@Valid RegisterVo registerVo){
        String message=service.RegisterUserByEmail(registerVo);
        return message==null ? RestBean.success() : RestBean.failure(400,message);
    }

    @PostMapping("/resetPassword")
    public RestBean<Void> resetPassword(@Valid ResetVO vo){
        String message=service.ResetPassword(vo);
        return message==null ? RestBean.success():RestBean.failure(400,message);
    }
}
