package com.practice.search.service.search

import com.google.gson.Gson
import com.practice.search.entity.SearchHistory
import com.practice.search.entity.SearchResult
import com.practice.search.entity.kapi.KakaoApiResponse
import com.practice.search.entity.napi.NaverApiResponse
import com.practice.search.exception.ResponseException
import com.practice.search.exception.ResponseExceptionCode
import com.practice.search.repository.SearchHistoryRepository
import com.practice.search.service.provider.KakaoApiProviderService
import com.practice.search.service.provider.NaverApiProviderService
import jakarta.transaction.Transactional
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.nio.charset.Charset

@Service
@Transactional
class SearchService(
    private val kakaoApiProvider: KakaoApiProviderService,
    private val naverApiProvider: NaverApiProviderService,
    private val searchHistoryRepository: SearchHistoryRepository,
    private val gson: Gson
) {
    fun searchBlogs(query: String, pageable: Pageable): SearchResult {
        validateParameter(query, pageable)

        val apiProviders = listOf(kakaoApiProvider, naverApiProvider)

        for (apiProvider in apiProviders) {
            try {
                val response = apiProvider.fetchData(query, pageable).block()

                when (apiProvider) {
                    is KakaoApiProviderService -> {
                        val searchResult = gson.fromJson(response, SearchResult::class.java)
                        if (searchResult.documents.isNotEmpty()) {
                            increaseSearchCount(query)
                            return searchResult
                        }
                    }

                    is NaverApiProviderService -> {
                        val naverResponse = gson.fromJson(response, NaverApiResponse::class.java)
                        if (naverResponse.items.isNotEmpty()) {
                            increaseSearchCount(query)
                            return SearchResult.of(naverResponse)
                        }
                    }
                    // add other open api
                }
            } catch (e: Exception) {
                continue
            }
        }

        throw ResponseException(ResponseExceptionCode.NO_SEARCH_RESULT)
    }

    // @Lock(value = LockModeType.PESSIMISTIC_WRITE) // fail test 100 requests concurrently
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