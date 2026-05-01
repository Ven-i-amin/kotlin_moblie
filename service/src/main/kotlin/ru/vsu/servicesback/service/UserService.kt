package ru.vsu.servicesback.service

import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.vsu.servicesback.dto.request.UserRequest
import ru.vsu.servicesback.dto.request.UserLoginRequest
import ru.vsu.servicesback.dto.response.UserResponse
import ru.vsu.servicesback.entity.UserEntity
import ru.vsu.servicesback.exception.ConflictException
import ru.vsu.servicesback.exception.InvalidPasswordException
import ru.vsu.servicesback.repository.UserRepository
import ru.vsu.servicesback.security.AuthContext
import ru.vsu.servicesback.security.JwtService

@Service
class UserService(
    private val userRepository: UserRepository,
    private val orderService: OrderService,
    private val jwtService: JwtService,
    private val authContext: AuthContext,
) {
    private var watchlistCleanup: ((UserEntity) -> Unit)? = null

    fun currentAuthenticatedEmail(): String = authContext.currentUserEmail()

    fun login(request: UserLoginRequest): UserResponse {
        val user = getEntityByEmail(request.email)
        if (user.password != request.password) {
            throw InvalidPasswordException("Invalid password")
        }

        return user.toResponse(jwtService.generateToken(user.email, requireNotNull(user.id)))
    }

    fun getById(id: Long): UserResponse {
        val user = getEntityById(id)
        authContext.requireEmail(user.email)
        return user.toResponse()
    }

    fun getByEmail(email: String): UserResponse {
        authContext.requireEmail(email)
        return getEntityByEmail(email).toResponse()
    }

    fun getEntityByEmail(email: String): UserEntity =
        userRepository.findByEmail(email)
            ?: throw EntityNotFoundException("User not found")

    fun getEntityByEmailOrTokenId(email: String, userId: Long?): UserEntity =
        userRepository.findByEmail(email)
            ?: userId?.let { id ->
                userRepository.findById(id).orElseThrow { EntityNotFoundException("User not found") }
            }
            ?: throw EntityNotFoundException("User not found")

    fun registerWatchlistCleanup(cleanup: (UserEntity) -> Unit) {
        watchlistCleanup = cleanup
    }

    @Transactional
    fun create(request: UserRequest) {
        if (userRepository.existsByEmail(request.email)) {
            throw ConflictException("User with email ${request.email} already exists")
        }

        userRepository.save(
            UserEntity(
                fullName = request.fullName,
                email = request.email,
                password = request.password,
                balance = request.balance,
            ),
        )
    }

    @Transactional
    fun update(id: Long, request: UserRequest) {
        val user = getEntityById(id)
        authContext.requireEmail(user.email)
        val emailTaken = userRepository.findByEmail(request.email)?.id?.let { it != id } ?: false
        if (emailTaken) {
            throw ConflictException("User with email ${request.email} already exists")
        }

        val oldEmail = user.email
        user.fullName = request.fullName
        user.email = request.email
        user.password = request.password
        user.balance = request.balance

        userRepository.save(user)

        if (oldEmail != request.email) {
            orderService.reassignUserEmail(oldEmail, request.email)
        }
    }

    @Transactional
    fun changeBalance(id: Long, deltaBalance: Double) {
        val user = getEntityById(id)
        authContext.requireEmail(user.email)
        val newBalance = user.balance + deltaBalance
        if (newBalance < 0) {
            throw IllegalArgumentException("Balance is less than 0")
        }

        user.balance = newBalance
        userRepository.save(user)
    }

    @Transactional
    fun delete(id: Long) {
        val user = getEntityById(id)
        authContext.requireEmail(user.email)
        orderService.deleteByEmail(user.email)
        watchlistCleanup?.invoke(user)
        userRepository.delete(user)
    }

    private fun getEntityById(id: Long): UserEntity =
        userRepository.findById(id)
            .orElseThrow { EntityNotFoundException("User not found") }

    private fun UserEntity.toResponse(token: String? = null): UserResponse =
        UserResponse(
            id = requireNotNull(id),
            fullName = fullName,
            email = email,
            password = password,
            balance = balance,
            token = token,
        )
}
