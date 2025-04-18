package com.example.forum.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.forum.Entity.Dto.Comment;
import com.example.forum.Entity.Vo.response.CommentWithUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    List<CommentWithUser> selectCommentWithUsernameByTid(@Param("tid") int tid);

    @Select("SELECT c.cid, c.content, c.time, a.user_name AS username, a.avatar " +
            "FROM db_comment c JOIN db_account a ON c.uid = a.user_id " +
            "WHERE c.tid = #{tid} AND c.top_comment_id = c.cid")
    List<CommentWithUser> selectTopLevelCommentsWithUserByTid(@Param("tid") int tid);

    @Select("SELECT c.cid, c.content, c.time, a.user_name AS username, a.avatar" +
            " FROM db_comment c JOIN db_account a ON c.uid = a.user_id " +
            "WHERE c.top_comment_id = #{topCid} AND c.cid != #{topCid} LIMIT 3")
    List<CommentWithUser> selectFirstThreeRepliesWithUserByTopCid(@Param("topCid") int topCid);

    @Select("SELECT c.cid, c.content, c.time, a.user_name AS username, a.avatar " +
            "FROM db_comment c JOIN db_account a ON c.uid = a.user_id WHERE " +
            "c.top_comment_id = #{topCid} && c.reply_cid !=-1 LIMIT #{offset}, #{pageSize}")
    List<CommentWithUser> selectPagedRepliesWithUserByTopCid(@Param("topCid") int topCid,
                                                             @Param("offset") int offset,
                                                             @Param("pageSize") int pageSize);
}
