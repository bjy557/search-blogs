package com.practice.search.app.repository

import com.practice.search.app.entity.search.SearchHistory
import org.springframework.data.jpa.repository.JpaRepository

interface SearchHistoryRepository : JpaRepository<SearchHistory, Long> {
    fun findByKeyword(keyword: String): SearchHistory?
    
    fun findTop10ByOrderByCountDesc(): List<SearchHistory>
}