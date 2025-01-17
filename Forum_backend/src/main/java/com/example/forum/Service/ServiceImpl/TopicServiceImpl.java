package com.example.forum.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.forum.Entity.Dto.Topic;
import com.example.forum.Entity.Dto.TopicCollect;
import com.example.forum.Entity.Dto.TopicLike;
import com.example.forum.Entity.Dto.UserCollects;
import com.example.forum.Entity.Vo.request.TopicCreatVO;
import com.example.forum.Mapper.TopicCollectMapper;
import com.example.forum.Mapper.TopicLikeMapper;
import com.example.forum.Mapper.TopicMapper;
import com.example.forum.Service.TopicService;
import jakarta.annotation.Resource;
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
        if (like==0&&!like(topicId,userId)) {
            likeMapper.insert(new TopicLike(topicId,userId,new Date()));
            return null;
        }
        if (like==1&&like(topicId,userId)){
            likeMapper.delete(new QueryWrapper<TopicLike>().eq("user_id",userId));
        }
        return null;
    }

    @Override
    public String changeCollect(int topicId, int userId,int collect) {
//        String hashKey= String.valueOf(topicId)+userId;
//        redisTemplate.opsForHash().put("topic_collect",hashKey,collect);
        if (collect==0&&!collect(topicId,userId)) {
            TopicCollect topicCollect= new TopicCollect(topicId,userId,new Date());
            collectMapper.insert(topicCollect);
            return null;
        }
        if (collect==1&&collect(topicId,userId)){
            collectMapper.delete(new QueryWrapper<TopicCollect>().eq("user_id",userId));
        }
        return null;
    }

    @Override
    public List<UserCollects> getCollects(int userId) {
        return mapper.selectCollectsByUserId(userId);
    }

    @Override
    public List<UserCollects> getLikes(int userId) {
        System.out.println(mapper.selectLikesByUserId(userId));
        return mapper.selectLikesByUserId(userId);
    }

    @Override
    public List<Topic> getMyTopics(int userId) {
        QueryWrapper<Topic> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        return mapper.selectList(queryWrapper);
    }

    @Override
    public String creatTopic(int userId, TopicCreatVO vo) {
        Topic topic=new Topic();
        topic.setTitle(vo.getTitle());
        topic.setCreatTime(new Date());
        topic.setUpdateTime(new Date());
        topic.setUserId(userId);
        topic.setText(vo.getText().toJSONString());
        if (mapper.insert(topic)==0){
            return "操作失败，请询问管理";
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
