package com.practice.search.data

import com.practice.search.entity.Content
import com.practice.search.entity.kapi.Document
import com.practice.search.entity.SearchHistory
import com.practice.search.entity.SearchResult
import com.practice.search.response.SearchBlogResponse
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

object TestDataGenerator {
    fun generateSearchResult(): SearchResult {
        return SearchResult(
            10,
            false,
            listOf(
                Content(
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
            10,
            false,
            emptyList()
        )
    }

    fun generateSearchBlogResponse(): SearchBlogResponse {
        val searchResult = generateSearchResult()

        return SearchBlogResponse(
            content = searchResult.contents,
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