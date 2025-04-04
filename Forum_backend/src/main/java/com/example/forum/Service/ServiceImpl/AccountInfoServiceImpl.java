package com.example.forum.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.forum.Entity.Dto.Account;
import com.example.forum.Entity.Dto.AccountInfo;
import com.example.forum.Entity.Vo.request.AccountInfoVO;
import com.example.forum.Mapper.AccountInfoMapper;
import com.example.forum.Mapper.UserMapper;
import com.example.forum.Service.AccountInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AccountInfoServiceImpl extends ServiceImpl<AccountInfoMapper, AccountInfo> implements AccountInfoService {
    @Resource
    UserMapper userMapper;

    @Override
    public Boolean updateUserInfo(int userId, AccountInfoVO vo) {
        AccountInfo accountInfo=this.getAccountInfoById(userId);
        if (accountInfo==null || accountInfo.getUserId()==userId){
            UpdateWrapper<Account> updateWrapper=new UpdateWrapper<>();
            updateWrapper.eq("user_id",userId).set("username",vo.getUsername());
            userMapper.update(updateWrapper);
            this.saveOrUpdate(new AccountInfo(userId,
                    vo.getUsername(),
                    vo.getSex(),
                    vo.getIntroduction(),
                    vo.getBirthday()));
            return true;
        }
        return false;
    }

    @Override
    public AccountInfo getAccountInfoById(int userId) {
        return this.getById(userId);
    }
}
