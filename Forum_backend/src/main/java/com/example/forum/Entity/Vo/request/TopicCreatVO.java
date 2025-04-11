package com.example.forum.Entity.Vo.request;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

@Data
public class TopicCreatVO {
    int type;
    String title;
    JSONObject text;
}
