package com.practice.search.app.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder

@Service
class WebClientService(
    private val webClient: WebClient,
    @Value("\${dapi.key}")
    private val authKey: String
) {
    fun fetchData(queryParams: Map<String, String>): String {
        val baseUrl = "https://dapi.kakao.com/v2/search/blog"

        val uri = UriComponentsBuilder.fromUriString(baseUrl)
            .apply {
                queryParams.forEach { (key, value) ->
                    queryParam(key, value)
                }
            }
            .build()
            .toUri()

        return webClient.get()
            .uri(uri)
            .header("Authorization", "KakaoAK ${authKey}")
            .retrieve()
            .bodyToMono(String::class.java)
            .block() ?: ""
    }
}