package com.practice.search.web.controller

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
        return ResponseEntity.ok(searchService.searchBlogs(query, pageable))
    }
}