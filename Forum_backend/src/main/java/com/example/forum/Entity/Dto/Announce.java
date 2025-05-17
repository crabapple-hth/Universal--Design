package com.example.forum.Entity.Dto;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("db_announce")
public class Announce {
    @TableId("id")
    Integer id;
    String content;
    Date creatTime;
}
