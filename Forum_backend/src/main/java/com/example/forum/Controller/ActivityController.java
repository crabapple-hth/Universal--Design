package com.example.forum.Controller;

import com.example.forum.Entity.Dto.Activity;
import com.example.forum.Entity.RestBean;
import com.example.forum.Service.ActivityService;
import com.example.forum.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {
    
    @Resource
    ActivityService activityService;
    
    @Resource
    JwtUtils jwtUtils;
    
    @GetMapping
    public RestBean<List<Activity>> getActivityList() {
        return RestBean.success(activityService.getActivityList());
    }
    
    @GetMapping("/{id}")
    public RestBean<Activity> getActivityDetail(@PathVariable Integer id) {
        Activity activity = activityService.getActivityDetail(id);
        if (activity == null) {
            return RestBean.failure(404, "活动不存在");
        }
        return RestBean.success(activity);
    }
    
    @PostMapping
    public RestBean<Void> createActivity(@RequestBody Activity activity) {
        if (activityService.createActivity(activity)) {
            return RestBean.success();
        }
        return RestBean.failure(400, "创建活动失败");
    }
    
    @PutMapping("/{id}")
    public RestBean<Void> updateActivity(@PathVariable Integer id, @RequestBody Activity activity) {
        activity.setId(id);
        if (activityService.updateActivity(activity)) {
            return RestBean.success();
        }
        return RestBean.failure(400, "更新活动失败");
    }
    
    @DeleteMapping("/{id}")
    public RestBean<Void> deleteActivity(@PathVariable Integer id) {
        if (activityService.deleteActivity(id)) {
            return RestBean.success();
        }
        return RestBean.failure(400, "删除活动失败");
    }
    
    @PostMapping("/{id}/register")
    public RestBean<Void> registerActivity(@PathVariable Integer id, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer userId = jwtUtils.getId(token);
        if (userId == 0) {
            return RestBean.failure(401, "未登录");
        }
        
        if (activityService.registerActivity(id, userId)) {
            return RestBean.success();
        }
        return RestBean.failure(400, "报名失败");
    }
    
    @PostMapping("/{id}/cancel")
    public RestBean<Void> cancelRegistration(@PathVariable Integer id, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer userId = jwtUtils.getId(token);
        if (userId == 0) {
            return RestBean.failure(401, "未登录");
        }
        
        if (activityService.cancelRegistration(id, userId)) {
            return RestBean.success();
        }
        return RestBean.failure(400, "取消报名失败");
    }
    
    @GetMapping("/{id}/check")
    public RestBean<Boolean> checkRegistration(@PathVariable Integer id, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer userId = jwtUtils.getId(token);
        if (userId == 0) {
            return RestBean.failure(401, "未登录");
        }
        
        boolean isRegistered = activityService.checkRegistration(id, userId);
        return RestBean.success(isRegistered);
    }
} 