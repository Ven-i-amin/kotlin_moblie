package ru.vsu.servicesback.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.vsu.servicesback.entity.UserEntity
import ru.vsu.servicesback.entity.WatchlistEntryEntity

interface WatchlistEntryRepository : JpaRepository<WatchlistEntryEntity, Long> {
    fun findAllByUser(user: UserEntity): List<WatchlistEntryEntity>
    fun existsByCoinIdAndUser(coinId: String, user: UserEntity): Boolean
    fun deleteByCoinIdAndUser(coinId: String, user: UserEntity)
    fun deleteAllByUser(user: UserEntity)
}
