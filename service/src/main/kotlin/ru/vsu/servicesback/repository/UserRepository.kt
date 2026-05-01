package ru.vsu.servicesback.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.vsu.servicesback.entity.UserEntity

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByEmail(email: String): UserEntity?
    fun existsByEmail(email: String): Boolean
}
