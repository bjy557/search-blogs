package com.practice.search.web.controller.api

import com.practice.search.app.entity.SearchResult
import com.practice.search.app.service.SearchService
import com.practice.search.web.response.SearchBlogResponse
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
    private val searchService: SearchService
) {
    @GetMapping("/blog")
    fun getBlogs(
        @RequestParam(value = "query", required = true) query: String,
        @PageableDefault(size = 10, page = 1, sort = ["accuracy", "recency"]) pageable: Pageable
    ): ResponseEntity<SearchBlogResponse> {
        val searchResult = searchService.searchBlogs(query, pageable)
        val totalElements = searchResult.meta.pageableCount
        val totalPages = ceil(totalElements / pageable.pageSize.toDouble()).toInt()
        
        return ResponseEntity.ok(
            SearchBlogResponse(
                searchResult.documents,
                pageable,
                totalElements,
                totalPages,
                searchResult.meta.isEnd
            )
        )
    }
}