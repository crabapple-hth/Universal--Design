package com.example.forum.Entity.Dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserCollects {
    private int topicId;
    private String title;
    private String text;
    private int userId;
    private Date creatTime;
    private Date updateTime;
    private Date collectTime;
}
