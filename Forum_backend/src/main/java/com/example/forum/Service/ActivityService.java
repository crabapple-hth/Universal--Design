package com.example.forum.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.forum.Entity.Dto.Activity;

import java.util.List;

public interface ActivityService extends IService<Activity> {
    // 获取活动列表
    List<Activity> getActivityList();
    
    // 获取活动详情
    Activity getActivityDetail(Integer id);
    
    // 创建活动
    boolean createActivity(Activity activity);
    
    // 更新活动
    boolean updateActivity(Activity activity);
    
    // 删除活动
    boolean deleteActivity(Integer id);
    
    // 报名活动
    boolean registerActivity(Integer activityId, Integer userId);
    
    // 取消报名
    boolean cancelRegistration(Integer activityId, Integer userId);
    
    // 检查用户是否已报名
    boolean checkRegistration(Integer activityId, Integer userId);
} 