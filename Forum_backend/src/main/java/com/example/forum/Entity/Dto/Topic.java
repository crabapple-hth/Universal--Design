package com.example.forum.Entity.Dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("db_topic")
@AllArgsConstructor
@NoArgsConstructor
public class Topic {
    @TableId("topic_id")
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
