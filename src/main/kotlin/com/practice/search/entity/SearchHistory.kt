package com.practice.search.entity

import jakarta.persistence.*

@Entity
@Table(name = "search_history", indexes = [Index(name = "idx_count", columnList = "count")])
data class SearchHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "keyword", nullable = false)
    val keyword: String = "",

    @Column(name = "count", nullable = false)
    var count: Int = 0
) {
    constructor(keyword: String, count: Int) : this(
        null,
        keyword,
        count
    )
}
