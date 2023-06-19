package com.practice.search.web.response

import com.practice.search.app.entity.search.Document
import org.springframework.data.domain.Pageable

data class SearchBlogResponse(
    val content: List<Document>,
    val pageable: Pageable,
    var totalElements: Int,
    var totalPages: Int,
    var last: Boolean
)
