package com.practice.search.app.service

import com.google.gson.Gson
import com.practice.search.app.entity.SearchHistory
import com.practice.search.app.entity.SearchResult
import com.practice.search.app.repository.SearchHistoryRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
@Transactional
class SearchService(
    private val webClientService: WebClientService,
    private val searchHistoryRepository: SearchHistoryRepository,
    private val gson: Gson,
    private val entityService: EntityService
) {
    fun searchBlogs(query: String, pageable: Pageable): SearchResult {
        val response = webClientService.fetchData(query, pageable).block()
        val searchResult = gson.fromJson(response, SearchResult::class.java)

        increaseSearchCount(query)

        return searchResult
    }

    fun increaseSearchCount(keyword: String) {
        val searchHistory = searchHistoryRepository.findByKeyword(keyword)
        searchHistory?.run {
            count++
            entityService.saveEntityWithLock(this) // 동시성 이슈 lock 추가
        } ?: entityService.saveEntityWithLock(SearchHistory(keyword, 1))
    }
}