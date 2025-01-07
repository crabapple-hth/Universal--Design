package com.example.forum.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.forum.Entity.Dto.Topic;
import com.example.forum.Entity.Dto.TopicCollect;
import com.example.forum.Entity.Dto.TopicLike;
import com.example.forum.Mapper.TopicCollectMapper;
import com.example.forum.Mapper.TopicLikeMapper;
import com.example.forum.Mapper.TopicMapper;
import com.example.forum.Service.TopicService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic> implements TopicService {

    @Resource
    TopicMapper mapper;


    @Resource
    TopicLikeMapper likeMapper;

    @Resource
    TopicCollectMapper collectMapper;

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

    @Override
    public List<Boolean> getTopicLikeCollect(int topicId, int userId) {
        List<Boolean> likeCollect=new ArrayList<>();
        likeCollect.add(like(topicId,userId));
        likeCollect.add(collect(topicId,userId));
        return likeCollect;
    }

    @Override
    public String changeLike(int topicId, int userId,int like) {
//        String hashKey= String.valueOf(topicId)+userId;
//        redisTemplate.opsForHash().put("topic_like",hashKey,like);
        if (like==1&&!like(topicId,userId)) {
            likeMapper.insert(new TopicLike());
            return null;
        }
        if (like==0&&like(topicId,userId)){
            likeMapper.delete(new QueryWrapper<TopicLike>().eq("user_id",userId));
        }
        return null;
    }

    @Override
    public String changeCollect(int topicId, int userId,int collect) {
//        String hashKey= String.valueOf(topicId)+userId;
//        redisTemplate.opsForHash().put("topic_collect",hashKey,collect);
        if (collect==1&&!collect(topicId,userId)) {
            collectMapper.insert(new TopicCollect());
            return null;
        }
        if (collect==0&&collect(topicId,userId)){
            collectMapper.delete(new QueryWrapper<TopicCollect>().eq("user_id",userId));
        }
        return null;
    }

    public Boolean like(int topic_id,int user_id){
        QueryWrapper<TopicLike> queryWrapper=new QueryWrapper<>();
        queryWrapper.allEq(Map.of("topic_id",topic_id,"user_id",user_id));
        TopicLike like=likeMapper.selectOne(queryWrapper);
        return like != null;
    }

    public Boolean collect(int topic_id,int user_id){
        QueryWrapper<TopicCollect> queryWrapper=new QueryWrapper<>();
        queryWrapper.allEq(Map.of("topic_id",topic_id,"user_id",user_id));
        TopicCollect collect=collectMapper.selectOne(queryWrapper);
        return collect != null;
    }

}
