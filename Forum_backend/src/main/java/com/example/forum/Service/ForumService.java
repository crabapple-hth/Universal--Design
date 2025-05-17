package com.example.forum.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.forum.Entity.Dto.Announce;
import com.example.forum.Entity.Vo.response.AnnounceVO;

import java.util.List;

public interface ForumService extends IService<Announce> {
    public List<Announce> getAnnounce();
    public Void delAnnounce(int id);
}
