package com.practice.search.app.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.practice.search.app.entity.SearchResult
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono

@Service
class WebClientService(
    @Value("\${dapi.key}")
    private val authKey: String,
    private val webClient: WebClient,
) {
    fun fetchData(query: String, pageable: Pageable): Mono<String> {
        val baseUrl = "https://dapi.kakao.com/v2/search/blog"
        
        // sort property만 추출, direction 값을 같이 넘기면 정렬 안되는 이슈 
        val sort = pageable.sort.map { it.property }.toList()[0]

        val uri = UriComponentsBuilder.fromUriString(baseUrl)
            .queryParam("query", query)
            .queryParam("sort", sort)
            .queryParam("page", pageable.pageNumber)
            .queryParam("size", pageable.pageSize)
            .build()
            .toUri()

        return webClient.get()
            .uri(uri)
            .header("Authorization", "KakaoAK $authKey")
            .retrieve()
            .bodyToMono(String::class.java)
    }
}