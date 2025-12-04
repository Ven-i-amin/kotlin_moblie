package ru.vsu.task1.domain.models.coin

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailPlatformInfo(
    @SerialName("decimal_place")
    val decimalPlace: String?,
    @SerialName("contract_address")
    val contractAddress: String?
)
