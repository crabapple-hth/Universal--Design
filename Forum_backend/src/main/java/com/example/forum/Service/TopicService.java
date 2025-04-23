package com.example.forum.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.forum.Entity.Dto.Topic;
import com.example.forum.Entity.Dto.TopicType;
import com.example.forum.Entity.Dto.UserCollects;
import com.example.forum.Entity.Vo.request.CommentCreatVO;
import com.example.forum.Entity.Vo.request.TopicCreatVO;
import com.example.forum.Entity.Vo.response.CommentWithUser;
import com.example.forum.Entity.Vo.response.TopicDetailsVO;

import java.util.HashMap;
import java.util.List;


public interface TopicService extends IService<Topic> {
    public HashMap<String,List> getTopics(int current);
    public HashMap<String,List> getTopicsByType(int current,int type);
    public List<Boolean> getTopicLikeCollect(int topicId,int userId);
    public String changeLike(int topicId,int userId,int like);
    public String changeCollect(int topicId,int userId,int collect);
    public List<UserCollects> getCollects(int userId);
    public List<UserCollects> getLikes(int userId);
    public List<Topic> getMyTopics(int userId);
    public String creatTopic(int userId, TopicCreatVO vo);
    public String creatComment(int userId, CommentCreatVO vo);
    public TopicDetailsVO getTopicById(int topicId);
    public List<CommentWithUser> getTopLevelCommentsWithReplies(int tid);
    public List<CommentWithUser> getPagedReplies(int topCid, int pageNum, int pageSize);
    public List<TopicType> getTypeList();
    public String delTopic(int topicId);
    public TopicDetailsVO updateTopic(int topicId, TopicCreatVO vo);
    public Page<TopicDetailsVO> topicList(Page<?> page);
}
