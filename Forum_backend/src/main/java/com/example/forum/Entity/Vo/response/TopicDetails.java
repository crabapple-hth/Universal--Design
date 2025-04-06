package com.example.forum.Entity.Vo.response;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class TopicDetails {
    Integer topic_id;
    String title;
    String text;
    Date creat_time;
    Date update_time;
    String username;
    String avatar;
}
