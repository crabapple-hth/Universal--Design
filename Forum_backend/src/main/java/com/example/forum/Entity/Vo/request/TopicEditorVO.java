package com.example.forum.Entity.Vo.request;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopicEditorVO {
    int topicId;
    int type;
    String title;
    JSONObject text;
}
