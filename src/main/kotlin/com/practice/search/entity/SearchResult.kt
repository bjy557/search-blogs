package com.practice.search.entity

import com.practice.search.entity.kapi.KakaoApiResponse
import com.practice.search.entity.napi.NaverApiResponse

data class SearchResult(
    val pageableCount: Int = 0,
    val last: Boolean = false,
    val contents: List<Content> = emptyList(),
) {
    companion object {
        fun of(kakaoApiResponse: KakaoApiResponse) = SearchResult(
            pageableCount = kakaoApiResponse.meta.pageableCount,
            last = kakaoApiResponse.meta.isEnd,
            contents = kakaoApiResponse.documents.map(Content::of)
        )
        
        fun of(naverApiResponse: NaverApiResponse) = SearchResult(
            pageableCount = naverApiResponse.total,
            last = false,
            contents = naverApiResponse.items.map(Content::of),
        )
    }
}
