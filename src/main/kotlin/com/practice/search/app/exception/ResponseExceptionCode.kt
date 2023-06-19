package com.practice.search.app.exception

import org.springframework.http.HttpStatus

enum class ResponseExceptionCode(status: HttpStatus, code: Int, message: String) {
    // 2xx
    NO_SEARCH_RESULT(HttpStatus.OK, 204, "There is no content"),
    
    // 4xx
    INVALID_PAGE_NUMBER(HttpStatus.BAD_REQUEST, 400, "Page limit exceeded. Maximum limit is 50."),
    INVALID_SIZE_NUMBER(HttpStatus.BAD_REQUEST, 400, "Size limit exceeded. Maximum limit is 50."),
    INVALID_QUERY(HttpStatus.BAD_REQUEST, 400, "Input a query containing at least 2 character."),
    
    // 5xx
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "Internal server error");

    val status: HttpStatus = status
    val code: Int = code
    val message: String = message
}