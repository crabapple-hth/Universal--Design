package com.example.forum.Service.ServiceImpl;

import com.example.forum.Entity.Dto.TopicDocument;
import com.example.forum.repository.TopicSearchRepository;
import com.example.forum.Service.TopicSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicSearchServiceImpl implements TopicSearchService {

    private final TopicSearchRepository topicSearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public void saveTopic(TopicDocument topicDocument) {
        topicSearchRepository.save(topicDocument);
    }

    @Override
    public void deleteTopic(Integer topicId) {
        topicSearchRepository.deleteById(topicId);
    }

    @Override
    public Page<TopicDocument> searchTopics(String keyword, Integer type, Pageable pageable) {
        Criteria criteria = new Criteria()
                .or(new Criteria("title").matches(keyword))
                .or(new Criteria("text").matches(keyword));

        if (type != null) {
            criteria.and(new Criteria("type").is(type));
        }

        CriteriaQuery query = new CriteriaQuery(criteria).setPageable(pageable);
        SearchHits<TopicDocument> searchHits = elasticsearchOperations.search(query, TopicDocument.class);

        List<TopicDocument> content = searchHits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, searchHits.getTotalHits());
    }

}