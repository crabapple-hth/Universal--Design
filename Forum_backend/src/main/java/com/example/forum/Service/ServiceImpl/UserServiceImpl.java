package com.example.forum.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.forum.Entity.Dto.Account;
import com.example.forum.Entity.Dto.AccountInfo;
import com.example.forum.Entity.Vo.request.AccountInfoVO;
import com.example.forum.Entity.Vo.request.RegisterVo;
import com.example.forum.Entity.Vo.request.ResetVO;
import com.example.forum.Mapper.AccountInfoMapper;
import com.example.forum.Mapper.UserMapper;
import com.example.forum.Service.UserService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, Account> implements UserService {

    @Resource
    UserMapper userMapper;

    @Resource
    PasswordEncoder passwordEncoder;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    AccountInfoMapper accountInfoMapper;

    @Resource
    JavaMailSender sender;


    @Override//通过用户名获取用户数据
    public Account GetUserByUsername(String username) {
        QueryWrapper<Account> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_name",username);
        return userMapper.selectOne(queryWrapper);
    }

    public String VerifyCode(String email,int type){
        Random random=new Random();
        int code=random.nextInt(899999)+100000;
        SimpleMailMessage message=new SimpleMailMessage();
        if (type==1){
            message.setSubject("【htwx】注册验证码");
            message.setText("您请求的验证码为"+code+"有效时间为三分钟，请勿将验证码告知他人避免账号被他人使用");
        } else if (type==2) {
            message.setSubject("【htwx】重置密码验证码");
            message.setText("您请求的验证码为"+code+"有效时间为三分钟，请勿将验证码告知他人避免账号被他人使用");
        }
        message.setTo(email);
        message.setFrom("htwx123qq@163.com");
        sender.send(message);
        ValueOperations<String, String> operations=stringRedisTemplate.opsForValue();
        if (type==1){
            operations.set("RegisterEmailCode", String.valueOf(code),1, TimeUnit.MINUTES);
        }else if (type==2){
            operations.set("ResetEmailCode", String.valueOf(code),1, TimeUnit.MINUTES);
        }
        return null;
    }

    @Override
    public String getVerifyCode(String email,int type) {
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        if (type==1){
            if (!userMapper.exists(queryWrapper)) {
                return VerifyCode(email,type);
            }else {
                return "该邮箱已被注册";
            }
        } else if (type==2) {
            if (userMapper.exists(queryWrapper)){
                return VerifyCode(email,type);
            }else {
                return "用户不存在";
            }
        }
        return null;
    }

    @Override
    public Account GetUserById(int uid) {
        QueryWrapper<Account> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_id" ,uid);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public String ResetPassword(ResetVO vo) {
        String code=stringRedisTemplate.opsForValue().get("ResetEmailCode");
        if (!vo.getCode().equals(code)){
            return "验证码错误";
        }
        if (!userMapper.exists(new QueryWrapper<Account>().eq("email",vo.getEmail()))){
            return "用户不存在";
        }
        QueryWrapper<Account> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("email",vo.getEmail());
        Account account=userMapper.selectOne(queryWrapper);
        account.setPassword(passwordEncoder.encode(vo.getPassword()));
        userMapper.insertOrUpdate(account);
        return null;
    }


    @Override
    public String RegisterUserByEmail(RegisterVo registerVo) {
        String code=stringRedisTemplate.opsForValue().get("RegisterEmailCode");
        if (!registerVo.getCode().equals(code)){
            return "验证码错误";
        }
        if (userMapper.exists(new QueryWrapper<Account>().eq("user_name",registerVo.getUsername()))){
            return "用户名已被使用";
        }
        String email=registerVo.getEmail();
        String username=registerVo.getUsername();
        String password=passwordEncoder.encode(registerVo.getPassword());
        Account account=new Account(null,username,email,password,new Date(),
                "user",null,false,false);
        userMapper.insert(account);
        AccountInfo accountInfo=new AccountInfo(null,username,null,"MALE",new Date());
        accountInfoMapper.insert(accountInfo);
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
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }
}
