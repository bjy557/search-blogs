package com.practice.search.repository

import com.practice.search.entity.SearchHistory
import org.springframework.data.jpa.repository.JpaRepository

interface SearchHistoryRepository : JpaRepository<SearchHistory, Long> {
    fun findByKeyword(keyword: String): SearchHistory?
    
    fun findTop10ByOrderByCountDesc(): List<SearchHistory>
}