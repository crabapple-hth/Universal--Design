package com.example.forum.Entity.Dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.forum.Entity.BaseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("activity")
@AllArgsConstructor
@NoArgsConstructor
public class Activity implements BaseData {
    @TableId("id")
    private Integer id;
    
    private String title;
    
    private String description;
    
    @TableField("cover_image")
    private String coverImage;
    
    @TableField("start_time")
    private Date startTime;
    
    @TableField("end_time")
    private Date endTime;
    
    private String location;
    
    private String organizer;
    
    private String requirements;
    
    @TableField("max_participants")
    private Integer maxParticipants;
    
    @TableField("current_participants")
    private Integer currentParticipants;
    
    private Integer status;
    
    @TableField("create_time")
    private Date createTime;
    
    @TableField("update_time")
    private Date updateTime;
    
    @TableField("is_deleted")
    private Integer isDeleted;
} 