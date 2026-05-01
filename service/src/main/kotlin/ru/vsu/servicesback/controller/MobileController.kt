package ru.vsu.servicesback.controller

import jakarta.persistence.EntityNotFoundException
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.vsu.servicesback.dto.mobile.MobileBalanceRequest
import ru.vsu.servicesback.dto.mobile.MobileLoginRequest
import ru.vsu.servicesback.dto.mobile.MobileOrderCreateRequest
import ru.vsu.servicesback.dto.mobile.MobileOrderDto
import ru.vsu.servicesback.dto.mobile.MobileRegisterRequest
import ru.vsu.servicesback.dto.mobile.MobileTransactionDto
import ru.vsu.servicesback.dto.mobile.MobileUserCoinDto
import ru.vsu.servicesback.dto.mobile.MobileUserDto
import ru.vsu.servicesback.dto.mobile.MobileUserUpdateRequest
import ru.vsu.servicesback.dto.mobile.MobileWatchlistRequest
import ru.vsu.servicesback.dto.request.UserLoginRequest
import ru.vsu.servicesback.dto.request.UserRequest
import ru.vsu.servicesback.entity.OrderEntity
import ru.vsu.servicesback.entity.UserEntity
import ru.vsu.servicesback.service.OrderService
import ru.vsu.servicesback.service.UserCoinService
import ru.vsu.servicesback.service.UserService
import ru.vsu.servicesback.service.WatchlistService
import ru.vsu.servicesback.util.OrderStatus
import java.time.LocalDateTime
import java.time.ZoneOffset

@RestController
@RequestMapping("/api/mobile")
class MobileController(
    private val userService: UserService,
    private val userCoinService: UserCoinService,
    private val orderService: OrderService,
    private val watchlistService: WatchlistService,
) {
    @PostMapping("/auth/login")
    fun login(@Valid @RequestBody request: MobileLoginRequest): ResponseEntity<String> =
        jsonString(
            userService.login(UserLoginRequest(request.email, request.password)).token
                ?: throw EntityNotFoundException("Token not generated"),
        )

    @PostMapping("/auth/register")
    fun register(@Valid @RequestBody request: MobileRegisterRequest): ResponseEntity<String> {
        userService.create(
            UserRequest(
                fullName = request.name,
                email = request.email,
                password = request.password,
                balance = 0.0,
            ),
        )

        return jsonString(
            userService.login(UserLoginRequest(request.email, request.password)).token
                ?: throw EntityNotFoundException("Token not generated"),
        )
    }

    @PostMapping("/auth/logout")
    fun logout(): ResponseEntity<Unit> =
        ResponseEntity.ok().build()

    @GetMapping("/users/me")
    fun getCurrentUser(): MobileUserDto =
        userService.getEntityByEmail(userService.currentAuthenticatedEmail()).toMobileDto()

    @PutMapping("/users/me")
    fun updateCurrentUser(@Valid @RequestBody request: MobileUserUpdateRequest): MobileUserDto {
        val currentUser = userService.getEntityByEmail(userService.currentAuthenticatedEmail())
        userService.update(
            requireNotNull(currentUser.id),
            UserRequest(
                fullName = request.fullName,
                email = request.email,
                password = request.password,
                balance = request.balance,
            ),
        )

        return userService
            .getEntityByEmailOrTokenId(request.email, currentUser.id)
            .toMobileDto()
    }

    @PatchMapping("/users/me/balance")
    fun setBalance(@Valid @RequestBody request: MobileBalanceRequest): ResponseEntity<Unit> {
        val currentUser = userService.getEntityByEmail(userService.currentAuthenticatedEmail())
        userService.changeBalance(requireNotNull(currentUser.id), request.balance - currentUser.balance)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/user-coins")
    fun getUserCoins(): List<MobileUserCoinDto> =
        userCoinService.getByEmail(userService.currentAuthenticatedEmail()).map {
            MobileUserCoinDto(
                currencyId = it.currencyId,
                name = it.name,
                amount = it.amount,
            )
        }

    @GetMapping("/orders")
    fun getOrders(): List<MobileOrderDto> =
        orderService.getOrderEntitiesForCurrentUser().map { it.toMobileDto() }

    @PostMapping("/orders")
    fun createOrder(@Valid @RequestBody request: MobileOrderCreateRequest): List<MobileOrderDto> {
        orderService.createMobileOrder(
            email = userService.currentAuthenticatedEmail(),
            currencyId = request.currencyId,
            currencyName = request.currencyName,
            type = request.type,
            amount = request.amount,
            price = request.price,
        )

        return getOrders()
    }

    @GetMapping("/transactions")
    fun getTransactions(): List<MobileTransactionDto> =
        orderService.getOrderEntitiesForCurrentUser()
            .filter { it.status == OrderStatus.COMPLETED }
            .map { it.toMobileTransactionDto() }
            .sortedByDescending { it.timestamp }

    @GetMapping("/watchlist")
    fun getWatchlist(): List<String> =
        watchlistService.getCoinIdsForCurrentUser()

    @PostMapping("/watchlist")
    fun addToWatchlist(@Valid @RequestBody request: MobileWatchlistRequest): ResponseEntity<Unit> {
        watchlistService.addCoinForCurrentUser(request.coinId)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/watchlist/{coinId}")
    fun removeFromWatchlist(@PathVariable coinId: String): ResponseEntity<Unit> {
        watchlistService.removeCoinForCurrentUser(coinId)
        return ResponseEntity.ok().build()
    }

    private fun jsonString(value: String): ResponseEntity<String> =
        ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body("\"${value.replace("\\", "\\\\").replace("\"", "\\\"")}\"")

    private fun UserEntity.toMobileDto(): MobileUserDto =
        MobileUserDto(
            id = requireNotNull(id),
            fullName = fullName,
            email = email,
            password = password,
            balance = balance,
        )

    private fun OrderEntity.toMobileDto(): MobileOrderDto =
        MobileOrderDto(
            id = requireNotNull(id),
            currencyId = currencyId,
            currencyName = currencyName,
            type = type,
            amount = amount,
            price = exchangeAmount,
            status = status.value(),
        )

    private fun OrderEntity.toMobileTransactionDto(): MobileTransactionDto {
        val isBuy = type.equals("buy", ignoreCase = true)
        val timestampValue = (finishedAt ?: executedAt ?: LocalDateTime.now())
            .toInstant(ZoneOffset.UTC)
            .toEpochMilli()

        return MobileTransactionDto(
            id = requireNotNull(id).toString(),
            currencyId = currencyId,
            currencyAmount = if (isBuy) amount else -amount,
            changeCurrency = exchangeCurrencyId.ifBlank { "USD" },
            changeAmount = if (isBuy) -exchangeAmount else exchangeAmount,
            timestamp = timestampValue,
        )
    }
}
