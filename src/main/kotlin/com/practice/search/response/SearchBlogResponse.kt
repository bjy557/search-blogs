package com.practice.search.response

import com.practice.search.entity.Document
import org.springframework.data.domain.Pageable

data class SearchBlogResponse(
    val content: List<Document>,
    val pageable: Pageable,
    var totalElements: Int,
    var totalPages: Int,
    var last: Boolean
)
