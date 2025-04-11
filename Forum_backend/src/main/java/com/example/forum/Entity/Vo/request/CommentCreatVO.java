package com.example.forum.Entity.Vo.request;

import lombok.Data;

@Data
public class CommentCreatVO {
    int tid;
    String content;
    int reply_cid;
    int top_comment_id;
}
