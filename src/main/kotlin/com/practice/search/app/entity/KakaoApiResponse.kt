package com.practice.search.app.entity

data class KakaoApiResponse(
    val meta: Meta = Meta(),
    val documents: List<Document> = emptyList(),
)