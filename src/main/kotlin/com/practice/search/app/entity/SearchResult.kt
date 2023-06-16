package com.practice.search.app.entity

data class SearchResult(
    val meta: Meta = Meta(),
    val documents: List<Document> = emptyList(),
)
