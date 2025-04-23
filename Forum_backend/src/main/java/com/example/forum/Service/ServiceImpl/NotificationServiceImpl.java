package com.example.forum.Service.ServiceImpl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.forum.Entity.Dto.Notification;
import com.example.forum.Entity.Vo.response.NotificationVO;
import com.example.forum.Mapper.NotificationMapper;
import com.example.forum.Service.NotificationService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

    @Override
    public List<NotificationVO> findUserNotification(int uid) {
        return this.list(Wrappers.<Notification>query().eq("uid", uid))
                .stream()
                .map(notification -> notification.asViewObject(NotificationVO.class))
                .toList();
    }

    public void deleteUserNotification(int id, int uid){
        this.remove(Wrappers.<Notification>query().eq("id", id).eq("uid", uid));
    }

    public void deleteUserAllNotification(int uid){
        this.remove(Wrappers.<Notification>query().eq("uid", uid));
    }

    @Override
    public void addNotification(int uid, String title, String content, String type, String url, Date time) {
        Notification notification = new Notification();
        notification.setUid(uid);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setType(type);
        notification.setUrl(url);
        notification.setTime(time);
        this.save(notification);
    }
}