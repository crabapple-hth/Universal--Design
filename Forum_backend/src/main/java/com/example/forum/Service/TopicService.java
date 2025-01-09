package com.example.forum.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.forum.Entity.Dto.Topic;
import com.example.forum.Entity.Dto.UserCollects;

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
}
