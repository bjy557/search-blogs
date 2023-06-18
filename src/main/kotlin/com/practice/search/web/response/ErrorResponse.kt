package com.practice.search.web.response

data class ErrorResponse(
    val code: Int = 200,
    val message: String = "success"
)