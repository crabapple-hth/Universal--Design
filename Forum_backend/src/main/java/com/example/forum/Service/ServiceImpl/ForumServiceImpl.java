package com.example.forum.Service.ServiceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.forum.Entity.Dto.Announce;
import com.example.forum.Entity.Vo.response.AnnounceVO;
import com.example.forum.Mapper.ForumMapper;
import com.example.forum.Service.ForumService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForumServiceImpl extends ServiceImpl<ForumMapper, Announce> implements ForumService {

    @Resource
    ForumMapper mapper;

    @Override
    public List<Announce> getAnnounce() {
        return mapper.selectList(null);
    }

    @Override
    public Void delAnnounce(int id) {
        mapper.deleteById(id);
        return null;
    }




}
