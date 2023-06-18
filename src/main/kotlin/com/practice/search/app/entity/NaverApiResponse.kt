package com.practice.search.app.entity

data class NaverApiResponse(
    val items: List<Item> = emptyList(),
    val total: Int = 0,
    val start: Int = 0,
    val display: Int = 0,
    val lastBuildDate: String = ""
)