package com.practice.search.app.service

import com.google.gson.Gson
import com.practice.search.app.entity.SearchHistory
import com.practice.search.app.entity.SearchResult
import com.practice.search.app.repository.SearchHistoryRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

@Service
class SearchService(
    private val webClientService: WebClientService,
    private val searchHistoryRepository: SearchHistoryRepository,
    private val gson: Gson
) {
    fun searchBlogs(query: String, pageable: Pageable): SearchResult {
        val response = webClientService.fetchData(query, pageable).block()
        val searchResult = gson.fromJson(response, SearchResult::class.java)

        increaseSearchCount(query)

        return searchResult
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