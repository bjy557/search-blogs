package com.practice.search.app.entity

data class SearchResult(
    val pageableCount: Int = 0,
    val last: Boolean = false,
    val documents: List<Document> = emptyList(),
) {
    companion object {
        fun of(kakaoApiResponse: KakaoApiResponse) = SearchResult(
            pageableCount = kakaoApiResponse.meta.pageableCount,
            last = kakaoApiResponse.meta.isEnd,
            documents = kakaoApiResponse.documents
        )

        fun of(naverApiResponse: NaverApiResponse) = SearchResult(
            pageableCount = naverApiResponse.total,
            last = false,
            documents = naverApiResponse.items.map(Document::of),
        )
    }
}
