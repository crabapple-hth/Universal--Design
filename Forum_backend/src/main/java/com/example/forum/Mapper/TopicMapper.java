package com.example.forum.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.forum.Entity.Dto.Topic;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface TopicMapper extends BaseMapper<Topic> {
}
