package com.practice.search.entity

import com.practice.search.entity.alternative.NaverApiResponse

data class SearchResult(
    val pageableCount: Int = 0,
    val last: Boolean = false,
    val documents: List<Document> = emptyList(),
) {
    companion object {
        fun of(naverApiResponse: NaverApiResponse) = SearchResult(
            pageableCount = naverApiResponse.total,
            last = false,
            documents = naverApiResponse.items.map(Document.Companion::of),
        )
    }
}
