package com.example.forum.Entity.Vo.response;

import lombok.Data;

import java.util.Date;

@Data
public class AccountVO {
    Integer userid;
    String username;
    String email;
    String avatar;
    String role;
    Date creatTime;
    boolean mute;
    boolean banned;
}
