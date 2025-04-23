package com.example.forum.Entity.Vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicPreviewVO {
    Integer topicId;
    String title;
    String text;
    Integer type;
    Boolean top;
    Date creatTime;
    Date updateTime;
    Integer likeCount;
    Integer collectCount;
}
