package com.practice.search.app.exception

import org.springframework.http.HttpStatus

enum class ResponseExceptionCode(status: HttpStatus, message: String) {
    // 2xx
    OK(HttpStatus.OK, "success"),
    NO_SEARCH_RESULT(HttpStatus.NO_CONTENT, "There is no content"),
    
    // 4xx
    INVALID_PAGEABLE(HttpStatus.BAD_REQUEST, "Page limit exceeded. Maximum limit is 50."),
    INVALID_QUERY(HttpStatus.BAD_REQUEST, "Input a query containing at least 2 character."),
    
    // 5xx
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");

    val status: HttpStatus = status
    val message: String = message
}