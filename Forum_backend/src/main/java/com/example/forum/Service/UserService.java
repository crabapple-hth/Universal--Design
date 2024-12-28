package com.example.forum.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.forum.Entity.Account;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface UserService extends IService<Account>, UserDetailsService {
    public Account GetUserByUsername(String username);
    public String RegisterUserByEmail();
}
