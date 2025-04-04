package com.example.forum.Entity.Vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class AccountInfoVO {
    String username;
    String sex;
    Date birthday;
    String introduction;
}
