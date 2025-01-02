package com.example.forum.Controller;

import com.example.forum.Entity.RestBean;
import com.example.forum.Entity.Vo.request.RegisterVo;
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
                                    HttpServletRequest request){
        String message=service.getVerifyCode(email);
        return message==null ?  RestBean.success() : RestBean.failure(400,message);
    }

    @PostMapping("/register")
    public RestBean<Object> Register(@Valid RegisterVo registerVo){
        System.out.println(registerVo);
        String message=service.RegisterUserByEmail(registerVo);
        return message==null ? RestBean.success() : RestBean.failure(400,message);
    }
}
