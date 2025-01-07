package com.example.forum.Entity.Dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@TableName("db_topic_collect")
@AllArgsConstructor
public class TopicCollect {
    @TableField("topic_id")
    int topicId;
    @TableField("user_id")
    int userId;
    Date time;
}
