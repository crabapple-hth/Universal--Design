package com.example.forum.Entity.Dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("db_comment")
public class Comment {
    int cid;
    int uid;
    int tid;
    String content;
    Date time;
    @TableField("reply_cid")
    int replyCid;
    @TableField("top_comment_id")
    int topCid;
}
