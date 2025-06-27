package com.example.forum.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.forum.Entity.Dto.*;
import com.example.forum.Entity.Vo.request.CommentCreatVO;
import com.example.forum.Entity.Vo.request.TopicCreatVO;
import com.example.forum.Entity.Vo.request.TopicEditorVO;
import com.example.forum.Entity.Vo.response.CommentWithUser;
import com.example.forum.Entity.Vo.response.TopicDetailsVO;
import com.example.forum.Entity.Vo.response.TopicPreviewVO;
import com.example.forum.Mapper.*;
import com.example.forum.Service.NotificationService;
import com.example.forum.Service.TopicSearchService;
import com.example.forum.Service.TopicService;
import jakarta.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic> implements TopicService {

    private static final Logger log = LoggerFactory.getLogger(TopicServiceImpl.class);

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

    @Resource
    UserMapper userMapper;

    @Resource
    NotificationService notificationService;

    @Resource
    TopicSearchService searchService;


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
        topic.setTop(false);
        if (mapper.insert(topic)==0){
            return "操作失败，请询问管理";
        }
        
        // 获取数据库生成的ID
        Integer topicId = topic.getTopicId();
        if (topicId == null) {
            log.error("创建帖子后未获取到ID");
            return "操作失败，请询问管理";
        }
        
        // 创建并保存TopicDocument到Elasticsearch
        TopicDocument topicDocument = new TopicDocument();
        topicDocument.setTopicId(topicId);  // 使用数据库生成的ID
        topicDocument.setTitle(topic.getTitle());
        topicDocument.setContent(topic.getText());
        topicDocument.setType(topic.getType());
        topicDocument.setUserId(topic.getUserId());
        topicDocument.setCreatTime(topic.getCreatTime());
        topicDocument.setUpdateTime(topic.getUpdateTime());
        topicDocument.setTop(topic.getTop());
        topicDocument.setViewCount(0);
        topicDocument.setLikeCount(0);
        topicDocument.setCommentCount(0);
        
        try {
            searchService.saveTopic(topicDocument);
        } catch (Exception e) {
            // 记录错误日志，但不影响主流程
            log.error("保存帖子到Elasticsearch失败: {}", e.getMessage());
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
        Topic topic = baseMapper.selectById(vo.getTid());
        Account account = userMapper.selectById(userId);
        if(vo.getReply_cid() > 0) {
            Comment com = commentMapper.selectById(vo.getReply_cid());
            if(!Objects.equals(account.getUserid(), com.getUid())) {
                notificationService.addNotification(
                        com.getUid(),
                        "您有新的帖子评论回复",
                        account.getUsername()+" 回复了你发表的评论，快去看看吧！",
                        "success", "/topicDetails?tid="+com.getTid(),new Date()
                );
            }
        } else if (!Objects.equals(account.getUserid(), topic.getUserId())) {
            notificationService.addNotification(
                    topic.getUserId(),
                    "您有新的帖子回复",
                    account.getUsername()+" 回复了你发表的帖子: "+topic.getTitle()+"，快去看看吧！",
                    "success", "/topicDetails?tid="+topic.getUserId(),new Date()
            );
        }
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
        existingTopic.setUpdateTime(new Date());
        int rowsUpdated = mapper.updateById(existingTopic);
        if (rowsUpdated > 0) {
            return mapper.selectById(topicId).asViewObject(TopicDetailsVO.class);
        }
        return null;
    }

    @Override
    public Page<TopicDetailsVO> topicList(Page<?> page) {
        return mapper.topicList(page);
    }

    @Override
    public Integer countTodayTopics() {
        return mapper.countTodayTopics();
    }

    @Override
    public List<Map<String, Object>> countTopicsByType() {
        return mapper.countTopicsByType();
    }

    @Override
    public List<Map<String, Object>> countLastSevenDaysTopics(){
        return mapper.countLastSevenDaysTopics();
    }

    @Override
    public TopicDetailsVO editor(TopicEditorVO vo) {
        QueryWrapper<Topic> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("topic_id",vo.getTopicId());
        Topic topic=mapper.selectOne(queryWrapper);
        topic.setTitle(vo.getTitle());
        topic.setType(vo.getType());
        topic.setText(String.valueOf(vo.getText()));
        topic.setUpdateTime(new Date());
        mapper.insertOrUpdate(topic);
        return getTopicById(vo.getTopicId());
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
