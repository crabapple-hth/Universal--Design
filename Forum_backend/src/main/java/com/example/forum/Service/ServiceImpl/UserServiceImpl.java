package com.example.forum.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.forum.Entity.Account;
import com.example.forum.Mapper.UserMapper;
import com.example.forum.Service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, Account> implements UserService {

    @Resource
    UserMapper userMapper;

    @Resource
    PasswordEncoder passwordEncoder;

    @Override//通过用户名获取用户数据
    public Account GetUserByUsername(String username) {
        QueryWrapper<Account> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_name",username);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public String RegisterUserByEmail() {
        return null;
    }

    @Override//通过security来进行用户验证
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account=GetUserByUsername(username);
        if (account==null){
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        return User
                .withUsername(username)
                .password(account.getUser_password())
                .roles(account.getRole())
                .build();
    }
}
