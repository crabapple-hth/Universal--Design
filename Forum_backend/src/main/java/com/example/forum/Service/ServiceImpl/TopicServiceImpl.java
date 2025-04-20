package com.example.forum.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.forum.Entity.Dto.*;
import com.example.forum.Entity.Vo.request.CommentCreatVO;
import com.example.forum.Entity.Vo.request.TopicCreatVO;
import com.example.forum.Entity.Vo.response.CommentWithUser;
import com.example.forum.Entity.Vo.response.TopicDetailsVO;
import com.example.forum.Entity.Vo.response.TopicPreviewVO;
import com.example.forum.Mapper.*;
import com.example.forum.Service.TopicService;
import jakarta.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic> implements TopicService {

    @Resource
    TopicMapper mapper;

    @Resource
    TopicTypeMapper typeMapper;

    @Resource
    TopicLikeMapper likeMapper;

    @Resource
    CommentMapper commentMapper;

    @Resource
    TopicCollectMapper collectMapper;


    @Override
    public HashMap<String,List> getTopics(int current) {
        int pageSize = 10;
        Page<Topic> page = new Page<>(current, pageSize);
        QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();
        return getStringListHashMap(page, queryWrapper);
    }

    @Override
    public HashMap<String, List> getTopicsByType(int current, int type) {
        int pageSize = 10;
        Page<Topic> page = new Page<>(current, pageSize);
        QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type);
        return getStringListHashMap(page, queryWrapper);
    }

    @NotNull
    private HashMap<String, List> getStringListHashMap(Page<Topic> page, QueryWrapper<Topic> queryWrapper) {
        Page<Topic> topicPage = mapper.selectPage(page, queryWrapper);

        List<TopicPreviewVO> topicPreviewVOList = topicPage.getRecords().stream()
                .map(topic -> {
                    TopicPreviewVO vo = topic.asViewObject(TopicPreviewVO.class);
                    vo.setLikeCount(mapper.likeCount(topic.getTopicId()));
                    vo.setCollectCount(mapper.collectCount(topic.getTopicId()));
                    return vo;
                })
                .collect(Collectors.toList());

        HashMap<String, List> topicInfo = new HashMap<>();
        topicInfo.put("topics", topicPreviewVOList);
        topicInfo.put("total", Collections.singletonList(topicPage.getTotal()));
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
        topic.setType(vo.getType());
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

    @Override
    public String creatComment(int userId, CommentCreatVO vo) {
        Comment comment=new Comment();
        comment.setUid(userId);
        comment.setTid(vo.getTid());
        comment.setContent(vo.getContent());
        comment.setTime(new Date());
        comment.setReplyCid(vo.getReply_cid());
        comment.setTopCid(vo.getTop_comment_id());
        System.out.println(vo);
        commentMapper.insert(comment);
        return null;
    }

    @Override
    public TopicDetailsVO getTopicById(int topicId) {
        TopicDetailsVO topicDetails=new TopicDetailsVO();
        topicDetails.setLikeCount(mapper.likeCount(topicId));
        topicDetails.setCollectCount(mapper.collectCount(topicId));
        BeanUtils.copyProperties(mapper.selectTopicDetails(topicId).get(0),topicDetails,"likeCount","collectCount");
        return topicDetails;
    }


    @Override
    public List<CommentWithUser> getTopLevelCommentsWithReplies(int tid) {
        List<CommentWithUser> topLevelComments = commentMapper.selectTopLevelCommentsWithUserByTid(tid);
        for (CommentWithUser topLevelComment : topLevelComments) {
            List<CommentWithUser> replies = commentMapper.selectFirstThreeRepliesWithUserByTopCid(topLevelComment.getCid());
            topLevelComment.setReplies(replies);
        }
        return topLevelComments;
    }

    @Override
    public List<CommentWithUser> getPagedReplies(int topCid, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return commentMapper.selectPagedRepliesWithUserByTopCid(topCid, offset, pageSize);
    }

    @Override
    public List<TopicType> getTypeList() {
        return typeMapper.selectList(null);
    }

    @Override
    public String delTopic(int topicId) {
        mapper.deleteById(topicId);
        return null;
    }

    @Override
    public TopicDetailsVO updateTopic(int topicId, TopicCreatVO vo) {
        Topic existingTopic = mapper.selectById(topicId);
        if (existingTopic == null) {
            return null;
        }
        existingTopic.setType(vo.getType());
        existingTopic.setTitle(vo.getTitle());
        existingTopic.setText(vo.getText().toString());
        existingTopic.setUpdateTime(new Date()); // Update the update time
        int rowsUpdated = mapper.updateById(existingTopic);
        if (rowsUpdated > 0) {
            return mapper.selectById(topicId).asViewObject(TopicDetailsVO.class);
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
