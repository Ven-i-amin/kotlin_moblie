package ru.vsu.servicesback.dto.mobile

import com.fasterxml.jackson.annotation.JsonProperty

data class MobileLoginRequest(
    val email: String,
    val password: String,
)

data class MobileRegisterRequest(
    val email: String,
    val password: String,
    val name: String,
)

data class MobileBalanceRequest(
    val balance: Double,
)

data class MobileWatchlistRequest(
    val coinId: String,
)

data class MobileOrderCreateRequest(
    val currencyId: String,
    val currencyName: String,
    val type: String,
    val amount: Double,
    val price: Double,
)

data class MobileUserDto(
    val id: Long,
    @JsonProperty("full_name")
    val fullName: String,
    val email: String,
    val password: String,
    val balance: Double,
)

data class MobileUserUpdateRequest(
    val id: Long? = null,
    @JsonProperty("full_name")
    val fullName: String,
    val email: String,
    val password: String,
    val balance: Double,
)

data class MobileUserCoinDto(
    @JsonProperty("currency_id")
    val currencyId: String,
    val name: String,
    val amount: Double,
)

data class MobileOrderDto(
    val id: Long,
    val currencyId: String,
    val currencyName: String,
    val type: String,
    val amount: Double,
    val price: Double,
    val status: String,
)

data class MobileTransactionDto(
    val id: String,
    @JsonProperty("currency_id")
    val currencyId: String,
    @JsonProperty("currency_amount")
    val currencyAmount: Double,
    @JsonProperty("change_currency")
    val changeCurrency: String,
    @JsonProperty("change_amount")
    val changeAmount: Double,
    val timestamp: Long,
)
