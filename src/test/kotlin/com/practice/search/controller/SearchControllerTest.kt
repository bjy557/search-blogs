package com.practice.search.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.practice.search.app.entity.Document
import com.practice.search.app.entity.Meta
import com.practice.search.app.entity.SearchResult
import com.practice.search.app.exception.InvalidPageableException
import com.practice.search.app.exception.NoSearchResultException
import com.practice.search.app.exception.SearchServiceException
import com.practice.search.app.service.SearchService
import com.practice.search.web.response.SearchBlogResponse
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

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

    // open api 테스트에 대하여 dummy data를 임의로 받아서 테스트하도록 구현함.
    @Test
    fun `Test searchBlogs endpoint`() {
        val expectedResponse = generateSearchBlogResponse()
        
        val pageable = PageRequest.of(1, 10, Sort.by("accuracy"))
        `when`(searchService.searchBlogs("test", pageable)).thenReturn(expectedResponse)

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
    
    private fun generateSearchBlogResponse(): SearchBlogResponse {
        val searchResult = SearchResult(
            Meta(totalCount = 100, pageableCount = 10, isEnd = false),
            listOf(
                Document(
                    title = "test blog",
                    contents = "this is test blog",
                    url = "https://example.com/test-blog",
                    blogname = "test blog",
                    thumbnail = "https://example.com/test-blog/thumbnail.jpg",
                    datetime = "2023-06-17T10:00:00"
                )
            )
        )

        return SearchBlogResponse(
            content = searchResult.documents,
            pageable = PageRequest.of(1, 10, Sort.by("accuracy")),
            totalElements = searchResult.meta.pageableCount,
            totalPages = 1,
            last = searchResult.meta.isEnd
        )
    }
}