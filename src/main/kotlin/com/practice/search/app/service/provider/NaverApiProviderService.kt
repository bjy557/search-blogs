package com.practice.search.app.service.provider

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono

@Service
class NaverApiProviderService(
    @Value("\${api.naver.baseUrl}")
    private val baseUrl: String,
    @Value("\${api.naver.clientId}")
    private val clientId: String,
    @Value("\${api.naver.clientSecret}")
    private val clientSecret: String,
    private val webClient: WebClient,
) : ApiProviderService {
    override fun fetchData(query: String, pageable: Pageable): Mono<String> {
        // naver api query param 형태에 맞게 modify
        val sort =
            if (pageable.sort.map { it.property }.toList()[0].equals("accuracy"))
                "sim"
            else
                "date"

        val uri = UriComponentsBuilder.fromUriString(baseUrl)
            .queryParam("query", query)
            .queryParam("sort", sort)
            .queryParam("start", pageable.pageNumber)
            .queryParam("display", pageable.pageSize)
            .build()
            .toUri()

        return webClient.get()
            .uri(uri)
            .header("X-Naver-Client-Id", clientId)
            .header("X-Naver-Client-Secret", clientSecret)
            .retrieve()
            .bodyToMono(String::class.java)
    }
}