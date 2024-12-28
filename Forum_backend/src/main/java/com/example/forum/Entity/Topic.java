package com.example.forum.Entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@TableName("db_topic")
@AllArgsConstructor
public class Topic {
    Integer topic_id;
    String title;
    String text;
    Integer user_id;
    Date creat_time;
    Date update_time;
}
