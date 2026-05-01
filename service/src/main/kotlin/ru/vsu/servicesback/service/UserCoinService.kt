package ru.vsu.servicesback.service

import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.vsu.servicesback.dto.request.UserCoinRequest
import ru.vsu.servicesback.dto.request.UserCoinTransactionRequest
import ru.vsu.servicesback.dto.response.UserCoinResponse
import ru.vsu.servicesback.entity.UserCoinEntity
import ru.vsu.servicesback.exception.ConflictException
import ru.vsu.servicesback.repository.UserCoinRepository
import ru.vsu.servicesback.security.AuthContext

@Service
class UserCoinService(
    private val userService: UserService,
    private val userCoinRepository: UserCoinRepository,
    private val authContext: AuthContext,
) {

    fun getById(id: Long): UserCoinResponse {
        val userCoin = userCoinRepository.findById(id)
            .orElseThrow { EntityNotFoundException("User coin not found") }
        authContext.requireEmail(userCoin.user.email)
        return userCoin.toResponse()
    }

    fun getByEmail(email: String): List<UserCoinResponse> {
        authContext.requireEmail(email)
        val user = userService.getEntityByEmail(email)
        return userCoinRepository.findAllByUser(user)
            .map { it.toResponse() }
    }

    fun getByCurrencyIdAndEmail(currencyId: String, email: String): UserCoinResponse {
        authContext.requireEmail(email)
        return getEntityByCurrencyIdAndEmail(currencyId, email).toResponse()
    }

    @Transactional
    fun create(request: UserCoinRequest) {
        authContext.requireEmail(request.ownerEmail)
        val user = userService.getEntityByEmail(request.ownerEmail)
        if (userCoinRepository.existsByCurrencyIdAndUser(request.currencyId, user)) {
            throw ConflictException(
                "User coin with currencyId ${request.currencyId} and email ${request.ownerEmail} already exists",
            )
        }

        userCoinRepository.save(
            UserCoinEntity(
                currencyId = request.currencyId,
                name = request.name,
                amount = request.amount,
                user = user,
            ),
        )
    }

    @Transactional
    fun update(id: Long, request: UserCoinRequest) {
        val userCoin = userCoinRepository.findById(id)
            .orElseThrow { EntityNotFoundException("User coin not found") }
        authContext.requireEmail(userCoin.user.email)
        authContext.requireEmail(request.ownerEmail)
        val user = userService.getEntityByEmail(request.ownerEmail)
        val existing = userCoinRepository.findByCurrencyIdAndUser(request.currencyId, user)
        if (existing != null && existing.id != id) {
            throw ConflictException(
                "User coin with currencyId ${request.currencyId} and email ${request.ownerEmail} already exists",
            )
        }

        userCoin.currencyId = request.currencyId
        userCoin.name = request.name
        userCoin.amount = request.amount
        userCoin.user = user
        userCoinRepository.save(userCoin)
    }

    @Transactional
    fun changeAmount(request: UserCoinRequest) {
        authContext.requireEmail(request.ownerEmail)
        val userCoin = getEntityByCurrencyIdAndEmail(request.currencyId, request.ownerEmail)
        val newAmount = userCoin.amount + request.amount
        if (newAmount < 0) {
            throw IllegalArgumentException("Amount is negative")
        }

        userCoin.amount = newAmount
        userCoinRepository.save(userCoin)
    }

    @Transactional
    fun transaction(request: UserCoinTransactionRequest) {
        authContext.requireEmail(request.oldUserEmail)
        val oldUserCoin = getEntityByCurrencyIdAndEmail(request.currencyId, request.oldUserEmail)
        if (oldUserCoin.amount < request.changeAmount) {
            throw IllegalArgumentException("${request.changeAmount} is greater than ${oldUserCoin.amount}")
        }

        oldUserCoin.amount -= request.changeAmount

        val newUser = userService.getEntityByEmail(request.newUserEmail)
        val newUserCoin = userCoinRepository.findByCurrencyIdAndUser(request.currencyId, newUser)
            ?: UserCoinEntity(
                currencyId = oldUserCoin.currencyId,
                name = oldUserCoin.name,
                amount = 0.0,
                user = newUser,
            )

        newUserCoin.amount += request.changeAmount

        userCoinRepository.save(oldUserCoin)
        userCoinRepository.save(newUserCoin)
    }

    @Transactional
    fun delete(id: Long) {
        val userCoin = userCoinRepository.findById(id)
            .orElseThrow { EntityNotFoundException("User coin not found") }
        authContext.requireEmail(userCoin.user.email)
        userCoinRepository.delete(userCoin)
    }

    private fun getEntityByCurrencyIdAndEmail(currencyId: String, email: String): UserCoinEntity {
        val user = userService.getEntityByEmail(email)
        return userCoinRepository.findByCurrencyIdAndUser(currencyId, user)
            ?: throw EntityNotFoundException("User coin not found")
    }

    private fun UserCoinEntity.toResponse(): UserCoinResponse =
        UserCoinResponse(
            id = requireNotNull(id),
            currencyId = currencyId,
            name = name,
            amount = amount,
            ownerEmail = user.email,
        )
}
