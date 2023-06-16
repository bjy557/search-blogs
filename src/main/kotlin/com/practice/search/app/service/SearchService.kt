package com.practice.search.app.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.practice.search.app.entity.SearchResult
import com.practice.search.web.response.SearchBlogResponse
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import kotlin.math.ceil

@Service
class SearchService(
    private val webClientService: WebClientService,
    private val objectMapper: ObjectMapper
) {
    fun searchBlogs(query: String, pageable: Pageable): SearchBlogResponse {
        val response = webClientService.fetchData(query, pageable).block()
        val searchResult = objectMapper.readValue(response, SearchResult::class.java)
        val totalElements = searchResult.meta.pageableCount
        val totalPages = ceil(totalElements / pageable.pageSize.toDouble()).toInt()
        return SearchBlogResponse(searchResult.documents, pageable, totalElements, totalPages, searchResult.meta.isEnd)
    }
}