package com.example.forum.Entity.Vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentWithUser {
    int cid;
    String content;
    Date time;
    String username;
    String avatar;
    List<CommentWithUser> replies;
}
