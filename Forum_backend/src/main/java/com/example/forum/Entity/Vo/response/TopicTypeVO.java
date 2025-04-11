package com.example.forum.Entity.Vo.response;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopicTypeVO {
    int id;
    String name;
    String desc;
}
