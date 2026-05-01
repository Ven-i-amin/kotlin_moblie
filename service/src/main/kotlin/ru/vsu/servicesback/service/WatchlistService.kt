package ru.vsu.servicesback.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.vsu.servicesback.entity.UserEntity
import ru.vsu.servicesback.entity.WatchlistEntryEntity
import ru.vsu.servicesback.repository.WatchlistEntryRepository

@Service
class WatchlistService(
    private val watchlistEntryRepository: WatchlistEntryRepository,
    private val userService: UserService,
) {
    init {
        userService.registerWatchlistCleanup(::deleteAllForUser)
    }

    fun getCoinIdsForCurrentUser(): List<String> {
        val user = userService.getEntityByEmail(userService.currentAuthenticatedEmail())
        return watchlistEntryRepository.findAllByUser(user).map { it.coinId }
    }

    @Transactional
    fun addCoinForCurrentUser(coinId: String) {
        val user = userService.getEntityByEmail(userService.currentAuthenticatedEmail())
        if (!watchlistEntryRepository.existsByCoinIdAndUser(coinId, user)) {
            watchlistEntryRepository.save(WatchlistEntryEntity(coinId = coinId, user = user))
        }
    }

    @Transactional
    fun removeCoinForCurrentUser(coinId: String) {
        val user = userService.getEntityByEmail(userService.currentAuthenticatedEmail())
        watchlistEntryRepository.deleteByCoinIdAndUser(coinId, user)
    }

    @Transactional
    fun deleteAllForUser(user: UserEntity) {
        watchlistEntryRepository.deleteAllByUser(user)
    }
}
