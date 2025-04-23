package com.example.forum.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.forum.Entity.Dto.Topic;
import com.example.forum.Entity.Dto.UserCollects;
import com.example.forum.Entity.Vo.response.TopicDetailsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface TopicMapper extends BaseMapper<Topic> {
    @Select("SELECT e.topic_id,e.title,e.text,e.update_time,e.creat_time,e.user_id,R.time as collectTime FROM `db_topic` " +
            "AS e LEFT JOIN db_topic_collect AS R ON e.topic_id=R.topic_id WHERE R.user_id=#{userId}")
    List<UserCollects> selectCollectsByUserId(@Param("userId") int userId);

    @Select("SELECT e.topic_id,e.title,e.text,e.update_time,e.creat_time,e.user_id,R.time as collectTime FROM `db_topic` AS e RIGHT JOIN db_topic_likes AS R ON e.topic_id=R.topic_id WHERE R.user_id=#{userId}")
    List<UserCollects> selectLikesByUserId(@Param("userId") int userId);

    @Select("SELECT c.topic_id as tid, c.title,c.text,c.type, c.top,c.creat_time as creatTime," +
            "c.update_time as updateTime, a.user_name, a.avatar FROM db_topic  c "+
            "JOIN " +
            "db_account a ON c.user_id = a.user_id " +
            "WHERE " +
            "c.topic_id = #{tid}")
    List<TopicDetailsVO> selectTopicDetails(@Param("tid") int tid);

    @Select("SELECT COUNT(*) FROM db_topic_likes WHERE topic_id = #{tid}")
    Integer likeCount(@Param("tid") int tid);

    @Select("SELECT COUNT(*) FROM db_topic_collect WHERE topic_id = #{tid}")
    Integer collectCount(@Param("tid") int tid);

    @Select("SELECT c.topic_id as tid, c.title, c.text, c.type,c.top, c.creat_time as creatTime," +
            " c.update_time as updateTime, a.user_name, a.avatar " +
            "FROM db_topic c " +
            "JOIN db_account a ON c.user_id = a.user_id")

    Page<TopicDetailsVO> topicList(Page<?> page);

}
