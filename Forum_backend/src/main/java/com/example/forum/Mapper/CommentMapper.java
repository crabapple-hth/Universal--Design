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
    @Select(
            "SELECT c.cid, c.content, c.time, c.quote, a.user_name FROM db_comment  c "+
                    "JOIN " +
                    "db_account a ON c.uid = a.user_id " +
                    "WHERE " +
                    "c.tid = #{tid}"
    )
    List<CommentWithUser> selectCommentWithUsernameByTid(@Param("tid") int tid);
}
