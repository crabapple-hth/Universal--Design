package com.example.forum.Controller;

import com.example.forum.Entity.Dto.TopicDocument;
import com.example.forum.Entity.RestBean;
import com.example.forum.Entity.Vo.response.SearchResultVO;
import com.example.forum.Service.TopicSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class TopicSearchController {
    
    private final TopicSearchService topicSearchService;
    
    @GetMapping("/topics")
    public RestBean<SearchResultVO> searchTopics(
            @RequestParam String keyword,
            @RequestParam(required = false) Integer type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updateTime"));
        Page<TopicDocument> results = topicSearchService.searchTopics(keyword, type, pageable);
        
        return RestBean.success(SearchResultVO.fromPage(results));
    }
} 