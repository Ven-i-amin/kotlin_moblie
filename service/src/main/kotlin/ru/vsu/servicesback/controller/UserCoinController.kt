package ru.vsu.servicesback.controller

import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.vsu.servicesback.dto.request.UserCoinRequest
import ru.vsu.servicesback.dto.request.UserCoinTransactionRequest
import ru.vsu.servicesback.dto.response.UserCoinResponse
import ru.vsu.servicesback.service.UserCoinService

@Validated
@RestController
@RequestMapping("/api/usercoin")
class UserCoinController(
    private val userCoinService: UserCoinService,
) {

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): UserCoinResponse =
        userCoinService.getById(id)

    @GetMapping("/byEmail")
    fun getByEmail(
        @RequestParam(required = false) email: String?,
        @RequestBody(required = false) bodyEmail: String?,
    ): List<UserCoinResponse> = userCoinService.getByEmail(resolveEmail(email, bodyEmail))

    @GetMapping("/{currencyId}/byEmail")
    fun getByCurrencyIdAndEmail(
        @PathVariable currencyId: String,
        @RequestParam(required = false) email: String?,
        @RequestBody(required = false) bodyEmail: String?,
    ): UserCoinResponse = userCoinService.getByCurrencyIdAndEmail(currencyId, resolveEmail(email, bodyEmail))

    @PostMapping("", "/")
    fun create(@Valid @RequestBody request: UserCoinRequest) {
        userCoinService.create(request)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: UserCoinRequest) {
        userCoinService.update(id, request)
    }

    @PatchMapping("/amount")
    fun changeAmount(@Valid @RequestBody request: UserCoinRequest) {
        userCoinService.changeAmount(request)
    }

    @PatchMapping("/transaction")
    fun transaction(@Valid @RequestBody request: UserCoinTransactionRequest) {
        userCoinService.transaction(request)
    }

    private fun resolveEmail(email: String?, bodyEmail: String?): String =
        email?.takeIf { it.isNotBlank() }
            ?: bodyEmail?.trim()?.removeSurrounding("\"")?.takeIf { it.isNotBlank() }
            ?: throw IllegalArgumentException("Email is required")
}
