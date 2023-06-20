package com.practice.search.service.provider

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono

@Service
class KakaoApiProviderService(
    @Value("\${api.kakao.baseUrl}")
    private val baseUrl: String,
    @Value("\${api.kakao.authKey}")
    private val authKey: String,
    private val webClient: WebClient,
) : ApiProviderService {
    override fun fetchData(query: String, pageable: Pageable): Mono<String> {
        // sort property만 추출, direction 값을 같이 넘기면(ex. accuracy:ASC) 정렬 안되는 이슈 
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