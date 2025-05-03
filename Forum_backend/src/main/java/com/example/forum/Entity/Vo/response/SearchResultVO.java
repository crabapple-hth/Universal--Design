package com.example.forum.Entity.Vo.response;

import com.example.forum.Entity.Dto.TopicDocument;
import lombok.Data;

import java.util.List;

@Data
public class SearchResultVO {
    private List<TopicDocument> content;
    private long total;
    private int currentPage;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
    
    public static SearchResultVO fromPage(org.springframework.data.domain.Page<TopicDocument> page) {
        SearchResultVO vo = new SearchResultVO();
        vo.setContent(page.getContent());
        vo.setTotal(page.getTotalElements());
        vo.setCurrentPage(page.getNumber());
        vo.setTotalPages(page.getTotalPages());
        vo.setHasNext(page.hasNext());
        vo.setHasPrevious(page.hasPrevious());
        return vo;
    }
} 