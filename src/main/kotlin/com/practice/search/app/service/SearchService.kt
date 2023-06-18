package com.practice.search.app.service

import com.google.gson.Gson
import com.practice.search.app.entity.SearchHistory
import com.practice.search.app.entity.SearchResult
import com.practice.search.app.exception.ResponseException
import com.practice.search.app.exception.ResponseExceptionCode
import com.practice.search.app.repository.SearchHistoryRepository
import jakarta.persistence.LockModeType
import jakarta.transaction.Transactional
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Lock
import org.springframework.stereotype.Service
import java.nio.charset.Charset

@Service
@Transactional
class SearchService(
    private val webClientService: WebClientService,
    private val searchHistoryRepository: SearchHistoryRepository,
    private val gson: Gson
) {
    fun searchBlogs(query: String, pageable: Pageable): SearchResult {
        validateParameter(query, pageable)

        val response = webClientService.fetchData(query, pageable).block()
        val searchResult = gson.fromJson(response, SearchResult::class.java)

        if (searchResult.documents.isEmpty()) {
            throw ResponseException(ResponseExceptionCode.NO_SEARCH_RESULT)
        }

        increaseSearchCount(query)

        return searchResult
    }

//    @Lock(value = LockModeType.PESSIMISTIC_WRITE) // fail test 100 requests concurrently
    @Synchronized // success test 100 requests concurrently
    fun increaseSearchCount(keyword: String) {
        val searchHistory = searchHistoryRepository.findByKeyword(keyword)
        searchHistory?.run {
            count++
            searchHistoryRepository.save(searchHistory)
        } ?: searchHistoryRepository.save(SearchHistory(keyword, 1))
    }

    fun validateParameter(query: String, pageable: Pageable) {
        if (pageable.pageNumber > 50) {
            throw ResponseException(ResponseExceptionCode.INVALID_PAGE_NUMBER)
        }
        
         if (pageable.pageSize > 50) {
            throw ResponseException(ResponseExceptionCode.INVALID_SIZE_NUMBER)
        }

        if (query.toByteArray(Charset.forName("euc-kr")).size < 3) { // 영어 3자, 한글 2자 제한
            throw ResponseException(ResponseExceptionCode.INVALID_QUERY)
        }
    }
}