package com.practice.search.app.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class Meta(
    @JsonProperty("total_count")
    val totalCount: Int = 0,
    
    @JsonProperty("pageable_count")
    val pageableCount: Int = 0,
    
    @JsonProperty("is_end")
    val isEnd: Boolean = false
)