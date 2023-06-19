package com.practice.search.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.practice.search.app.dto.SearchHistoryDto
import com.practice.search.app.exception.ResponseException
import com.practice.search.app.exception.ResponseExceptionCode
import com.practice.search.app.service.search.SearchHistoryService
import com.practice.search.app.service.search.SearchService
import com.practice.search.data.TestDataGenerator
import com.practice.search.web.response.SearchHistoryResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
@AutoConfigureMockMvc
class SearchControllerTest {

    // ref. https://youtrack.jetbrains.com/issue/IDEA-295144/IDE-doesnt-see-autowired-mockmvc
    // IntelliJ 21.2.4 에러 -> 23.1.2 업데이트 후 해결 (MockMvc no such bean 이슈)
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var searchService: SearchService
    
    @MockBean
    private lateinit var searchHistoryService: SearchHistoryService

    // open api 테스트의 경우 dummy data를 임의로 받거나 직접 예외를 던져서 테스트하도록 구현함.
    @Test
    fun `Test searchBlogs endpoint`() {
        val expectedSearchResult = TestDataGenerator.generateSearchResult()
        val expectedResponse = TestDataGenerator.generateSearchBlogResponse()

        val pageable = PageRequest.of(1, 10, Sort.by("accuracy"))
        `when`(searchService.searchBlogs("test", pageable)).thenReturn(expectedSearchResult)

        mockMvc.perform(
            get("/search/blog")
                .param("query", "test")
                .param("sort", "accuracy")
                .param("page", "1")
                .param("size", "10")
        )
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))
    }

    @Test
    fun `Test searchBlogs endpoint with no search result`() {
        val pageable = PageRequest.of(1, 10, Sort.by("accuracy"))
        `when`(searchService.searchBlogs("test", pageable))
            .thenThrow(ResponseException(ResponseExceptionCode.NO_SEARCH_RESULT))

        val result =
            mockMvc.perform(
                get("/search/blog")
                    .param("query", "test")
                    .param("sort", "accuracy")
                    .param("page", "1")
                    .param("size", "10")
            )
                .andExpect(status().isNoContent)    // check 204 code
                .andReturn()
                .response
                .contentAsString

        assertEquals(
            "There is no content",
            Gson().fromJson(result, JsonObject::class.java).get("message").asString
        ) // check message
    }

    @Test
    fun `Test searchBlogs endpoint with invalid page number`() {
        val pageable = PageRequest.of(51, 10, Sort.by("accuracy"))
        `when`(searchService.searchBlogs("test", pageable))
            .thenThrow(ResponseException(ResponseExceptionCode.INVALID_PAGE_NUMBER))

        val result =
            mockMvc.perform(
                get("/search/blog")
                    .param("query", "test")
                    .param("sort", "accuracy")
                    .param("page", "51") // page 50 제한
                    .param("size", "10")
            )
                .andExpect(status().isBadRequest)    // check 400 code
                .andReturn()
                .response
                .contentAsString

        assertEquals(
            "Page limit exceeded. Maximum limit is 50.",
            Gson().fromJson(result, JsonObject::class.java).get("message").asString
        )
    }
    
    @Test
    fun `Test searchBlogs endpoint with invalid size number`() {
        val pageable = PageRequest.of(1, 51, Sort.by("accuracy"))
        `when`(searchService.searchBlogs("test", pageable))
            .thenThrow(ResponseException(ResponseExceptionCode.INVALID_SIZE_NUMBER))

        val result =
            mockMvc.perform(
                get("/search/blog")
                    .param("query", "test")
                    .param("sort", "accuracy")
                    .param("page", "1")
                    .param("size", "51") // size 50 제한
            )
                .andExpect(status().isBadRequest)    // check 400 code
                .andReturn()
                .response
                .contentAsString

        assertEquals(
            "Size limit exceeded. Maximum limit is 50.",
            Gson().fromJson(result, JsonObject::class.java).get("message").asString
        )
    }

    @Test
    fun `Test searchBlogs endpoint with search service exception`() {

        val pageable = PageRequest.of(1, 10, Sort.by("accuracy"))
        `when`(searchService.searchBlogs("test", pageable))
            .thenThrow(ResponseException(ResponseExceptionCode.INTERNAL_SERVER_ERROR))

        val result =
            mockMvc.perform(
                get("/search/blog")
                    .param("query", "test")
                    .param("sort", "accuracy")
                    .param("page", "1")
                    .param("size", "10")
            )
                .andExpect(status().isInternalServerError)
                .andReturn()
                .response
                .contentAsString

        assertEquals(
            "Internal server error", 
            Gson().fromJson(result, JsonObject::class.java).get("message").asString
        )
    }
    
    @Test
    fun `Test getHistories`() {
        val searchHistoryDtos = TestDataGenerator.generateSearchHistories().map(SearchHistoryDto::of)

        `when`(searchHistoryService.findTop10Histories()).thenReturn(searchHistoryDtos)

        mockMvc.perform(get("/search/history"))
            .andExpect(status().isOk)
            .andExpect(content().string(Gson().toJson(SearchHistoryResponse(searchHistoryDtos))))
    }
}