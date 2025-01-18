package com.example.forum.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.forum.Entity.Dto.Topic;
import com.example.forum.Entity.Dto.UserCollects;
import com.example.forum.Entity.Vo.request.CommentCreatVO;
import com.example.forum.Entity.Vo.request.TopicCreatVO;

import java.util.HashMap;
import java.util.List;


public interface TopicService extends IService<Topic> {
    public HashMap<String,List> getTopics(int current);
    public List<Boolean> getTopicLikeCollect(int topicId,int userId);
    public String changeLike(int topicId,int userId,int like);
    public String changeCollect(int topicId,int userId,int collect);
    public List<UserCollects> getCollects(int userId);
    public List<UserCollects> getLikes(int userId);
    public List<Topic> getMyTopics(int userId);
    public String creatTopic(int userId, TopicCreatVO vo);
    public String creatCommend(int userId, CommentCreatVO vo);
    public Topic getTopicById(int topicId);
}
