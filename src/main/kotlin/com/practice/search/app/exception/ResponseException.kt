package com.practice.search.app.exception

class ResponseException(responseExceptionCode: ResponseExceptionCode): RuntimeException() {
    val responseExceptionCode: ResponseExceptionCode = responseExceptionCode
}