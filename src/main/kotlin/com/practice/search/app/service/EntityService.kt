package com.practice.search.app.service

import jakarta.persistence.EntityManager
import jakarta.persistence.LockModeType
import org.springframework.stereotype.Service

@Service
class EntityService(
    private val entityManager: EntityManager
) {
    fun <T : Any> saveEntityWithLock(entity: T) {
        entityManager.lock(entity, LockModeType.PESSIMISTIC_WRITE)
        entityManager.persist(entity)
    }
}