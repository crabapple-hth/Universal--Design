package com.example.forum.Service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.forum.Entity.Dto.Notification;
import com.example.forum.Entity.Vo.response.NotificationVO;

import java.util.Date;
import java.util.List;

public interface NotificationService extends IService<Notification> {
    List<NotificationVO> findUserNotification(int uid);
    void deleteUserNotification(int id, int uid);
    void deleteUserAllNotification(int uid);
    void addNotification(int uid, String title, String content, String type, String url, Date time);
}
