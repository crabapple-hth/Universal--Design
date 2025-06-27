package com.example.forum.Entity.Vo.response;


import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicDetailsVO {
    Integer tid;
    String title;
    String text;
    Integer type;
    Integer userId;
    Boolean top;
    Date creatTime;
    Date updateTime;
    String username;
    String avatar;
    Integer likeCount;
    Integer collectCount;
}
