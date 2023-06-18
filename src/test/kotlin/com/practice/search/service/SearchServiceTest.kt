package com.practice.search.service

import com.google.gson.Gson
import com.practice.search.app.entity.SearchHistory
import com.practice.search.app.exception.NoSearchResultException
import com.practice.search.app.repository.SearchHistoryRepository
import com.practice.search.app.service.SearchService
import com.practice.search.app.service.WebClientService
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
import reactor.core.publisher.Mono

class SearchServiceTest {
    @Mock
    private lateinit var webClientService: WebClientService

    @Mock
    private lateinit var searchHistoryRepository: SearchHistoryRepository

    private lateinit var searchService: SearchService

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        searchService = SearchService(webClientService, searchHistoryRepository, Gson())
    }

    // open api 테스트의 경우 dummy data를 임의로 받거나 직접 예외를 던져서 테스트하도록 구현함.
    @Test
    fun `Test searchBlogs endpoint with successful search result`() {
        val expectedSearchResult = TestDataGenerator.generateSearchResult()
        val expectedResponse = TestDataGenerator.generateSearchBlogResponse()
        
        val pageable = PageRequest.of(1, 10, Sort.by("accuracy"))
        val mockResponse = Gson().toJson(expectedSearchResult)
        `when`(webClientService.fetchData("test", pageable)).thenReturn(Mono.just(mockResponse))

        val response = searchService.searchBlogs("test", pageable)

        assertEquals(expectedResponse, response)
    }

    @Test
    fun `Test searchBlogs endpoint with no search result`() {
        val expectedSearchResult = TestDataGenerator.generateEmptySearchResult()

        val pageable = PageRequest.of(1, 10, Sort.by("accuracy"))
        val mockResponse = Gson().toJson(expectedSearchResult)
        `when`(webClientService.fetchData("test", pageable)).thenReturn(Mono.just(mockResponse))

        assertThrows<NoSearchResultException> { 
            searchService.searchBlogs("test", pageable)
        }
    }
    
    @Test
    fun `Test increaseSearchCount when search history exists`() {
        // 검색 수행 시 해당 keyword의 count를 증가시킴
        val keyword = "test"
        val existingSearchHistory = SearchHistory(keyword, 5)

        `when`(searchHistoryRepository.findByKeyword(keyword)).thenReturn(existingSearchHistory)
        `when`(searchHistoryRepository.save(existingSearchHistory)).thenReturn(existingSearchHistory)

        searchService.increaseSearchCount(keyword)

        assertEquals(6, existingSearchHistory.count)
    }
}