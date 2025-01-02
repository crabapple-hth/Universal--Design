package com.example.forum.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.forum.Entity.Dto.Topic;
import com.example.forum.Mapper.TopicMapper;
import com.example.forum.Service.TopicService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic> implements TopicService {

    @Resource
    TopicMapper mapper;

    @Override
    public List<Topic> getTopics() {
        return mapper.selectList(null);
    }
}
