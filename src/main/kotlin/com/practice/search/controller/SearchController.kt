package com.practice.search.controller

import com.practice.search.response.SearchBlogResponse
import com.practice.search.response.SearchHistoryResponse
import com.practice.search.service.search.SearchHistoryService
import com.practice.search.service.search.SearchService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import kotlin.math.ceil

@RestController
@RequestMapping("/search")
class SearchController(
    private val searchService: SearchService,
    private val searchHistoryService: SearchHistoryService
) {
    @GetMapping("/blog")
    fun getBlogs(
        @RequestParam(value = "query", required = true) query: String,
        @PageableDefault(size = 10, page = 1, sort = ["accuracy", "recency"]) pageable: Pageable
    ): ResponseEntity<SearchBlogResponse> {
        val searchResult = searchService.searchBlogs(query, pageable)
        val totalElements = searchResult.pageableCount
        val totalPages = ceil(totalElements / pageable.pageSize.toDouble()).toInt()
        
        return ResponseEntity.ok(
            SearchBlogResponse(
                searchResult.contents,
                pageable,
                totalElements,
                totalPages,
                searchResult.last
            )
        )
    }
    
    @GetMapping("/history")
    fun getHistories(): ResponseEntity<SearchHistoryResponse> {
        val popularKeywords = searchHistoryService.findTop10Histories()
        return ResponseEntity.ok(SearchHistoryResponse(popularKeywords))
    }
}