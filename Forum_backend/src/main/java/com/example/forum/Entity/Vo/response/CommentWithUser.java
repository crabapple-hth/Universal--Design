package com.example.forum.Entity.Vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class CommentWithUser {
    int cid;
    String content;
    Date time;
    int quote;
    String username;
}
