package com.example.forum.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.forum.Entity.Dto.Topic;

import java.util.List;


public interface TopicService extends IService<Topic> {
    public List<Topic> getTopics();
}
