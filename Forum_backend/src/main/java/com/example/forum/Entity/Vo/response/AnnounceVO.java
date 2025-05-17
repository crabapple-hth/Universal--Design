package com.example.forum.Entity.Vo.response;

import lombok.Data;

import java.util.Date;

@Data
public class AnnounceVO {
    Integer id;
    String content;
    Date creatTime;
}
