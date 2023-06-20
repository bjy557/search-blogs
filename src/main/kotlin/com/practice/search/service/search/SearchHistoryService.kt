package com.practice.search.service.search

import com.practice.search.dto.SearchHistoryDto
import com.practice.search.entity.SearchHistory
import com.practice.search.repository.SearchHistoryRepository
import org.springframework.stereotype.Service

@Service
class SearchHistoryService(
    private val searchHistoryRepository: SearchHistoryRepository
) {
    fun findTop10Histories(): List<SearchHistoryDto> {
        val searchHistories: List<SearchHistory> = searchHistoryRepository.findTop10ByOrderByCountDesc()
        return searchHistories.map(SearchHistoryDto::of)
    }
}