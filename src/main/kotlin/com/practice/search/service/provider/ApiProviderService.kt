package com.practice.search.service.provider

import org.springframework.data.domain.Pageable
import reactor.core.publisher.Mono

interface ApiProviderService {
    fun fetchData(query: String, pageable: Pageable): Mono<String>
}