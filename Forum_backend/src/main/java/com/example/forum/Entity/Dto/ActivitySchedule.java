package com.example.forum.Entity.Dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("activity_schedule")
@AllArgsConstructor
@NoArgsConstructor
public class ActivitySchedule {
    @TableId("id")
    private Integer id;
    
    @TableField("activity_id")
    private Integer activityId;
    
    @TableField("time_slot")
    private String timeSlot;
    
    private String content;
    
    @TableField("create_time")
    private Date createTime;
    
    @TableField("update_time")
    private Date updateTime;
} 