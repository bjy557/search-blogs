package com.practice.search.app.service

import com.practice.search.app.dto.SearchHistoryDto
import com.practice.search.app.entity.SearchHistory
import com.practice.search.app.repository.SearchHistoryRepository
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