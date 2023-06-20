package com.practice.search.exception

class ResponseException(responseExceptionCode: ResponseExceptionCode): RuntimeException() {
    val responseExceptionCode: ResponseExceptionCode = responseExceptionCode
}