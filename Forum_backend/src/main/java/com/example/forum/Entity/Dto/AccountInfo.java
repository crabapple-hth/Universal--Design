package com.example.forum.Entity.Dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@TableName("db_account_details")
public class AccountInfo {
    @TableId("user_id")
    Integer uid;
    @TableField("user_name")
    String username;
    @TableField("introduction")
    String introduction;
    @TableField("sex")
    String sex;
    @TableField("birthday")
    Date birthday;
}
