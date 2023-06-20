package com.practice.search.entity.kapi

data class KakaoApiResponse(
    val meta: Meta = Meta(),
    val documents: List<Document> = emptyList(),
)
