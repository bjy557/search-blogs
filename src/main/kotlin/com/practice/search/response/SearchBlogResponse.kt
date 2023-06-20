package com.practice.search.response

import com.practice.search.entity.Content
import org.springframework.data.domain.Pageable

data class SearchBlogResponse(
    val content: List<Content>,
    val pageable: Pageable,
    var totalElements: Int,
    var totalPages: Int,
    var last: Boolean
)
