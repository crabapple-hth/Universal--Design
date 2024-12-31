package com.example.forum.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.forum.Entity.Dto.Account;
import com.example.forum.Entity.Vo.request.RegisterVo;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends IService<Account>, UserDetailsService {
    public Account GetUserByUsername(String username);
    public String RegisterUserByEmail(RegisterVo registerVo);
    public String getVerifyCode(RegisterVo registerVo);
}
