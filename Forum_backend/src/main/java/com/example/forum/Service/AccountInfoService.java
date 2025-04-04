package com.example.forum.Service;

import com.example.forum.Entity.Dto.AccountInfo;
import com.example.forum.Entity.Vo.request.AccountInfoVO;

public interface AccountInfoService {
    public Boolean updateUserInfo(int userId, AccountInfoVO vo);
    public AccountInfo getAccountInfoById(int userId);
}
