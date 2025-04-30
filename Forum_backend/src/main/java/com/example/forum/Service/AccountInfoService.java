package com.example.forum.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.forum.Entity.Dto.AccountInfo;
import com.example.forum.Entity.Vo.request.AccountInfoVO;

import java.util.List;
import java.util.Map;

public interface AccountInfoService extends IService<AccountInfo> {
    public Boolean updateUserInfo(int userId, AccountInfoVO vo);
    public AccountInfo getAccountInfoById(int userId);
    List<Map<String, Object>> countUsersByGender();
}
