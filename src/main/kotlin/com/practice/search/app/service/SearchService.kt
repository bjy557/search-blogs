package com.practice.search.app.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.practice.search.app.entity.SearchHistory
import com.practice.search.app.entity.SearchResult
import com.practice.search.app.repository.SearchHistoryRepository
import com.practice.search.web.response.SearchBlogResponse
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import kotlin.math.ceil

@Service
class SearchService(
    private val webClientService: WebClientService,
    private val objectMapper: ObjectMapper,
    private val searchHistoryRepository: SearchHistoryRepository
) {
    fun searchBlogs(query: String, pageable: Pageable): SearchBlogResponse {
        val response = webClientService.fetchData(query, pageable).block()
        val searchResult = objectMapper.readValue(response, SearchResult::class.java)
        val totalElements = searchResult.meta.pageableCount
        val totalPages = ceil(totalElements / pageable.pageSize.toDouble()).toInt()

        increaseSearchCount(query)

        return SearchBlogResponse(searchResult.documents, pageable, totalElements, totalPages, searchResult.meta.isEnd)
    }
    
    // 동시성 제어
    @Transactional(isolation = Isolation.SERIALIZABLE)
    fun increaseSearchCount(keyword: String) {
        val searchHistory = searchHistoryRepository.findByKeyword(keyword)
        searchHistory?.run {
            count++
            searchHistoryRepository.save(this)
        } ?: searchHistoryRepository.save(SearchHistory(keyword, 1))
    }
}