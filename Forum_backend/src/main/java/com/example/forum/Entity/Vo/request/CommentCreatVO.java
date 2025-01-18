package com.example.forum.Entity.Vo.request;

import lombok.Data;

@Data
public class CommentCreatVO {
    int tid;
    String content;
    int quote;
}
