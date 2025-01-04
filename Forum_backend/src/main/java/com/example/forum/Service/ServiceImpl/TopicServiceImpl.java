package com.example.forum.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.forum.Entity.Dto.Topic;
import com.example.forum.Mapper.TopicMapper;
import com.example.forum.Service.TopicService;
import jakarta.annotation.Resource;
import net.sf.jsqlparser.statement.select.Top;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic> implements TopicService {

    @Resource
    TopicMapper mapper;

    @Override
    public HashMap<String,List> getTopics(int current) {
        int pageSize=10;

        Page<Topic> page=new Page<>(current,pageSize);
        QueryWrapper<Topic> queryWrapper=new QueryWrapper<>();
        Page<Topic> topics = mapper.selectPage(page, queryWrapper);
        long total=topics.getTotal();
        HashMap<String,List> topicInfo=new HashMap<>();
        topicInfo.put("total", Collections.singletonList(total));
        topicInfo.put("topics", topics.getRecords());
        return topicInfo;
    }
}
