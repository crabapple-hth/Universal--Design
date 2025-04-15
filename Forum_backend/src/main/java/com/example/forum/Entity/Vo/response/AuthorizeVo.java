package com.example.forum.Entity.Vo.response;

import lombok.Data;

import java.util.Date;

@Data
public class AuthorizeVo {
    String username;
    String role;
    String token;
    Date expire;
}
