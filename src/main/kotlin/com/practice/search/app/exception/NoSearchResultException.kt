package com.practice.search.app.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NO_CONTENT)
class NoSearchResultException(message: String): RuntimeException(message)