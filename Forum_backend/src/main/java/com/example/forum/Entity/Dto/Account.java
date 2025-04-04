package com.example.forum.Entity.Dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
//用户数据
@Data
@TableName("db_account")
@AllArgsConstructor
public class Account {
    @TableId("user_id")
    Integer userid;
    @TableField("user_name")
    String username;
    @TableField("email")
    String email;
    @TableField("user_password")
    String password;
    @TableField("creat_time")
    Date creatTime;
    @TableField("role")
    String role;
    @TableField("avatar")
    String avatar;
}
