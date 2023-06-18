package com.practice.search.app.dto

import com.practice.search.app.entity.SearchHistory

data class SearchHistoryDto(
    val keyword: String,
    val count: Int
) {
    companion object {
        fun of(searchHistory: SearchHistory) = SearchHistoryDto(
            keyword = searchHistory.keyword,
            count = searchHistory.count
        )
    }
}