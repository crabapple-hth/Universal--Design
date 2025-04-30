package com.example.forum.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.forum.Entity.Dto.Activity;
import com.example.forum.Entity.Dto.ActivityRegistration;
import com.example.forum.Mapper.ActivityMapper;
import com.example.forum.Mapper.ActivityRegistrationMapper;
import com.example.forum.Service.ActivityService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {
    
    @Resource
    ActivityRegistrationMapper registrationMapper;
    
    @Override
    public List<Activity> getActivityList() {
        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", 0)
                   .orderByDesc("create_time");
        return list(queryWrapper);
    }
    
    @Override
    public Activity getActivityDetail(Integer id) {
        return getById(id);
    }
    
    @Override
    @Transactional
    public boolean createActivity(Activity activity) {
        activity.setCreateTime(new Date());
        activity.setUpdateTime(new Date());
        activity.setIsDeleted(0);
        activity.setCurrentParticipants(0);
        activity.setStatus(1); // 未开始
        return save(activity);
    }
    
    @Override
    @Transactional
    public boolean updateActivity(Activity activity) {
        activity.setUpdateTime(new Date());
        return updateById(activity);
    }
    
    @Override
    @Transactional
    public boolean deleteActivity(Integer id) {
        Activity activity = new Activity();
        activity.setId(id);
        activity.setIsDeleted(1);
        activity.setUpdateTime(new Date());
        return updateById(activity);
    }
    
    @Override
    @Transactional
    public boolean registerActivity(Integer activityId, Integer userId) {
        // 检查活动是否存在且未满
        Activity activity = getById(activityId);
        if (activity == null || activity.getIsDeleted() == 1) {
            return false;
        }
        if (activity.getCurrentParticipants() >= activity.getMaxParticipants()) {
            return false;
        }
        
        // 检查是否已经报名
        QueryWrapper<ActivityRegistration> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activity_id", activityId)
                   .eq("user_id", userId)
                   .eq("status", 1);
        if (registrationMapper.selectCount(queryWrapper) > 0) {
            return false;
        }
        
        // 创建报名记录
        ActivityRegistration registration = new ActivityRegistration();
        registration.setActivityId(activityId);
        registration.setUserId(userId);
        registration.setStatus(1);
        registration.setCreateTime(new Date());
        registration.setUpdateTime(new Date());
        
        // 更新活动参与人数
        activity.setCurrentParticipants(activity.getCurrentParticipants() + 1);
        activity.setUpdateTime(new Date());
        
        return registrationMapper.insert(registration) > 0 && updateById(activity);
    }
    
    @Override
    @Transactional
    public boolean cancelRegistration(Integer activityId, Integer userId) {
        // 查找报名记录
        QueryWrapper<ActivityRegistration> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activity_id", activityId)
                   .eq("user_id", userId)
                   .eq("status", 1);
        ActivityRegistration registration = registrationMapper.selectOne(queryWrapper);
        if (registration == null) {
            return false;
        }
        
        // 更新报名状态
        registration.setStatus(0);
        registration.setUpdateTime(new Date());
        
        // 更新活动参与人数
        Activity activity = getById(activityId);
        activity.setCurrentParticipants(activity.getCurrentParticipants() - 1);
        activity.setUpdateTime(new Date());
        
        return registrationMapper.updateById(registration) > 0 && updateById(activity);
    }
    
    @Override
    public boolean checkRegistration(Integer activityId, Integer userId) {
        QueryWrapper<ActivityRegistration> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activity_id", activityId)
                   .eq("user_id", userId)
                   .eq("status", 1);
        return registrationMapper.selectCount(queryWrapper) > 0;
    }
} 