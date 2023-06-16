package com.practice.search.app.dto

import jakarta.persistence.*

@Entity
@Table(name = "search_history")
data class SearchHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
            
    @Column(name = "keyword", nullable = false)
    val keyWord: String = "",
    
    @Column(name = "count", nullable = false)
    val count: Int = 0
)
