package com.example.forum.repository;

import com.example.forum.Entity.Dto.TopicDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicSearchRepository extends ElasticsearchRepository<TopicDocument, Integer> {
} 