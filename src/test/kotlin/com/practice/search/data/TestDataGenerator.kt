package com.practice.search.data

import com.practice.search.app.entity.Document
import com.practice.search.app.entity.Meta
import com.practice.search.app.entity.SearchHistory
import com.practice.search.app.entity.SearchResult
import com.practice.search.web.response.SearchBlogResponse
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

object TestDataGenerator {
    fun generateSearchResult(): SearchResult {
        return SearchResult(
            Meta(totalCount = 100, pageableCount = 10, isEnd = false),
            listOf(
                Document(
                    title = "test blog",
                    contents = "this is test blog",
                    url = "https://example.com/test-blog",
                    blogname = "test blog",
                    thumbnail = "https://example.com/test-blog/thumbnail",
                    datetime = "2023-06-17T10:00:00"
                )
            )
        )
    }
    
    fun generateEmptySearchResult(): SearchResult {
        return SearchResult(
            Meta(totalCount = 100, pageableCount = 10, isEnd = false),
            emptyList()
        )
    }
    
    fun generateSearchBlogResponse(): SearchBlogResponse {
        val searchResult = generateSearchResult()

        return SearchBlogResponse(
            content = searchResult.documents,
            pageable = PageRequest.of(1, 10, Sort.by("accuracy")),
            totalElements = 10,
            totalPages = 1,
            last = false
        )
    }
    
    fun generateSearchHistories(): List<SearchHistory> {
        return listOf(
            SearchHistory("k1", 10),
            SearchHistory("k2", 9),
            SearchHistory("k3", 8),
            SearchHistory("k4", 7),
            SearchHistory("k5", 6),
            SearchHistory("k6", 5),
            SearchHistory("k7", 4),
            SearchHistory("k8", 3),
            SearchHistory("k9", 2),
            SearchHistory("k10", 1),
        )
    }
}