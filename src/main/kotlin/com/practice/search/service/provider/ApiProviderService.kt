package com.practice.search.service.provider

import com.practice.search.entity.SearchResult
import org.springframework.data.domain.Pageable
import reactor.core.publisher.Mono

interface ApiProviderService {
    fun fetchData(query: String, pageable: Pageable): Mono<String>
    fun extractSearchResult(response: String): SearchResult?
}