package com.example.forum.Entity.Dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@TableName("db_topic")
@AllArgsConstructor
public class Topic {
    @TableField("topic_id")
    Integer topicId;
    String title;
    String text;
    @TableField("user_id")
    Integer userId;
    @TableField("creat_time")
    Date creatTime;
    @TableField("update_time")
    Date updateTime;
}
