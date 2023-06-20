package com.practice.search.response

data class ErrorResponse(
    val code: Int = 200,
    val message: String = "success"
)