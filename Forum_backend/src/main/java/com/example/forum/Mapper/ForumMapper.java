package com.example.forum.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.forum.Entity.Dto.Announce;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ForumMapper extends BaseMapper<Announce> {
}
