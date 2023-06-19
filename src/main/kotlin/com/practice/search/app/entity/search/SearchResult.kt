package com.practice.search.app.entity.search

import com.practice.search.app.entity.search.alternative.NaverApiResponse

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
