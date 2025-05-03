package com.example.forum.Service;

import com.example.forum.Entity.Dto.TopicDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TopicSearchService {
    /**
     * 保存帖子到Elasticsearch
     */
    void saveTopic(TopicDocument topicDocument);
    
    /**
     * 根据ID删除帖子
     */
    void deleteTopic(Integer topicId);
    
    /**
     * 搜索帖子
     * @param keyword 搜索关键词
     * @param type 帖子类型（可选）
     * @param pageable 分页参数
     * @return 搜索结果
     */
    Page<TopicDocument> searchTopics(String keyword, Integer type, Pageable pageable);
} 