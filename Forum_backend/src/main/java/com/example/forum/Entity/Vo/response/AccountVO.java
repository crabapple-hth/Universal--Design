package com.example.forum.Entity.Vo.response;

import lombok.Data;

import java.util.Date;

@Data
public class AccountVO {
    String username;
    String email;
    String avatar;
    Date creatTime;
}
