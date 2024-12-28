package com.example.forum.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
//用户数据
@Data
@TableName("db_account")
@AllArgsConstructor
public class Account {
    Integer user_id;
    String user_name;
    String email;
    String user_password;
    Date creat_time;
    String role;
}
