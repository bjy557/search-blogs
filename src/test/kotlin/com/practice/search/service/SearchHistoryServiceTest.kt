package com.practice.search.service

import com.practice.search.app.repository.SearchHistoryRepository
import com.practice.search.app.service.SearchHistoryService
import com.practice.search.data.TestDataGenerator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class SearchHistoryServiceTest {
    @Mock
    private lateinit var searchHistoryRepository: SearchHistoryRepository

    private lateinit var searchHistoryService: SearchHistoryService

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        searchHistoryService = SearchHistoryService(searchHistoryRepository)
    }
    
    @Test
    fun `Test findTop10Histories`() {
        // 가짜 데이터 생성
        val searchHistories = TestDataGenerator.generateSearchHistories()

        // Mock 객체의 동작 설정
        `when`(searchHistoryRepository.findTop10ByOrderByCountDesc()).thenReturn(searchHistories)

        // 테스트 수행
        val result = searchHistoryService.findTop10Histories()

        // 결과 검증
        assertEquals(10, result.size)
        assertEquals(10, result[0].count)
        assertEquals(9, result[1].count)
    }
    
    @Test
    fun `Test findTop10Histories with no content`() {
        // Mock 객체의 동작 설정
        `when`(searchHistoryRepository.findTop10ByOrderByCountDesc()).thenReturn(emptyList())

        // 테스트 수행
        val result = searchHistoryService.findTop10Histories()

        // 결과 검증
        assertEquals(0, result.size)
    }
}