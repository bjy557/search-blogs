package com.practice.search.app.exception

import com.practice.search.web.response.ErrorResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ResponseException::class)
    fun handleResponseException(ex: ResponseException): ResponseEntity<ErrorResponse> {
        val re = ex.responseExceptionCode
        return ResponseEntity.status(re.status)
            .body(ErrorResponse(re.code, re.message))
    }
}