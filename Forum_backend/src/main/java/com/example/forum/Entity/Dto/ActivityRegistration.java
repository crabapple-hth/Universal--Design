package com.example.forum.Entity.Dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("activity_registration")
@AllArgsConstructor
@NoArgsConstructor
public class ActivityRegistration {
    @TableId("id")
    private Integer id;
    
    @TableField("activity_id")
    private Integer activityId;
    
    @TableField("user_id")
    private Integer userId;
    
    private Integer status;
    
    @TableField("create_time")
    private Date createTime;
    
    @TableField("update_time")
    private Date updateTime;
} 