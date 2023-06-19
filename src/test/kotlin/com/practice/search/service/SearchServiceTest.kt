package com.practice.search.service

import com.google.gson.Gson
import com.practice.search.app.entity.search.SearchHistory
import com.practice.search.app.exception.ResponseException
import com.practice.search.app.repository.SearchHistoryRepository
import com.practice.search.app.service.provider.KakaoApiProviderService
import com.practice.search.app.service.provider.NaverApiProviderService
import com.practice.search.app.service.search.SearchService
import com.practice.search.data.TestDataGenerator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import reactor.core.publisher.Mono
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

class SearchServiceTest {
    @Mock
    private lateinit var kakaoApiProvider: KakaoApiProviderService
    
    @Mock
    private lateinit var naverApiProvider: NaverApiProviderService

    @Mock
    private lateinit var searchHistoryRepository: SearchHistoryRepository
    
    private lateinit var searchService: SearchService

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        searchService = SearchService(kakaoApiProvider, naverApiProvider, searchHistoryRepository, Gson())
    }

    // open api 테스트의 경우 dummy data를 임의로 받거나 직접 예외를 던져서 테스트하도록 구현함.
    @Test
    fun `Test searchBlogs endpoint with successful search result`() {
        val expectedSearchResult = TestDataGenerator.generateSearchResult()

        val pageable = PageRequest.of(1, 10, Sort.by("accuracy"))
        val mockResponse = Gson().toJson(expectedSearchResult)
        `when`(kakaoApiProvider.fetchData("test", pageable)).thenReturn(Mono.just(mockResponse))

        val response = searchService.searchBlogs("test", pageable)

        assertEquals(expectedSearchResult, response)
    }

    @Test
    fun `Test searchBlogs endpoint with no search result`() {
        val expectedSearchResult = TestDataGenerator.generateEmptySearchResult()

        val pageable = PageRequest.of(1, 10, Sort.by("accuracy"))
        val mockResponse = Gson().toJson(expectedSearchResult)
        `when`(kakaoApiProvider.fetchData("test", pageable)).thenReturn(Mono.just(mockResponse))

        val exception = assertThrows<ResponseException> {
            searchService.searchBlogs("test", pageable)
        }

        assertEquals(HttpStatus.NO_CONTENT, exception.responseExceptionCode.status)
    }

    @Test
    fun `Test increaseSearchCount when search history exists`() {
        // 검색 수행 시 해당 keyword의 count를 증가시킴
        val keyword = "test"
        val existingSearchHistory = SearchHistory(keyword, 5)

        `when`(searchHistoryRepository.findByKeyword(keyword)).thenReturn(existingSearchHistory)

        searchService.increaseSearchCount(keyword)

        assertEquals(6, existingSearchHistory.count)
    }

    @Test
    fun `Test increaseSearchCount 100 requests concurrently`() {
        val keyword = "test"
        val threadCount = 100
        val latch = CountDownLatch(threadCount)
        
        `when`(searchHistoryRepository.findByKeyword(keyword)).thenReturn(SearchHistory("test", 0))

        val executor = Executors.newFixedThreadPool(threadCount)
        repeat(threadCount) {
            executor.submit {
                searchService.increaseSearchCount(keyword)
                latch.countDown()
            }
        }

        latch.await()

        val searchHistory = searchHistoryRepository.findByKeyword(keyword)
        assertEquals(threadCount, searchHistory?.count)
    }
}