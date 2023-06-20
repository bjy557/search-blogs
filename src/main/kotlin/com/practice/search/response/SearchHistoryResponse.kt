package com.practice.search.response

import com.practice.search.dto.SearchHistoryDto

data class SearchHistoryResponse(
    val keywords: List<SearchHistoryDto>
)