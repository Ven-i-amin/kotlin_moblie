package ru.vsu.servicesback.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.vsu.servicesback.entity.UserCoinEntity
import ru.vsu.servicesback.entity.UserEntity

interface UserCoinRepository : JpaRepository<UserCoinEntity, Long> {
    fun findAllByUser(user: UserEntity): List<UserCoinEntity>
    fun findByCurrencyIdAndUser(currencyId: String, user: UserEntity): UserCoinEntity?
    fun existsByCurrencyIdAndUser(currencyId: String, user: UserEntity): Boolean
}
