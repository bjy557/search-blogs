package com.practice.search.service.search

import com.google.gson.Gson
import com.practice.search.entity.SearchHistory
import com.practice.search.entity.SearchResult
import com.practice.search.entity.kapi.KakaoApiResponse
import com.practice.search.entity.napi.NaverApiResponse
import com.practice.search.exception.ResponseException
import com.practice.search.exception.ResponseExceptionCode
import com.practice.search.repository.SearchHistoryRepository
import com.practice.search.service.provider.ApiProviderService
import com.practice.search.service.provider.KakaoApiProviderService
import com.practice.search.service.provider.NaverApiProviderService
import jakarta.transaction.Transactional
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.nio.charset.Charset
import java.time.Duration

@Service
@Transactional
class SearchService(
    private val kakaoApiProvider: KakaoApiProviderService,
    private val naverApiProvider: NaverApiProviderService,
    private val searchHistoryRepository: SearchHistoryRepository,
    private val gson: Gson
) {
    fun searchBlogs(query: String, pageable: Pageable): SearchResult? {
        validateParameter(query, pageable)
        
        for (apiProvider in listOf(kakaoApiProvider, naverApiProvider)) {
            try {
                val response = apiProvider.fetchData(query, pageable)
                    .block(Duration.ofSeconds(5))
                    ?: continue // timeout 발생 시 alt api로 전환
                
                increaseSearchCount(query)
                return extractSearchResult(apiProvider, response)

            } catch (e: Exception) {
                // 최초 api(kapi) 장애 시 alt api로 전환
                continue
            }
        }

        return null
    }

    fun extractSearchResult(apiProvider: ApiProviderService, response: String): SearchResult? {
        return when (apiProvider) {
            is KakaoApiProviderService -> apiProvider.extractSearchResult(response)
            is NaverApiProviderService -> apiProvider.extractSearchResult(response)
            // add other open api
            else -> null
        }
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