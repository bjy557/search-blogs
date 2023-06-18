package com.practice.search.web.response

import com.practice.search.app.dto.SearchHistoryDto

data class SearchHistoryResponse(
    val keywords: List<SearchHistoryDto>
)