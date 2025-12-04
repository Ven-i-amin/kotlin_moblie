package ru.vsu.task1.domain.models.coin;

import kotlinx.serialization.SerialName;
import kotlinx.serialization.Serializable;

@Serializable
data class DetailPlatforms(
    @SerialName("")
    val platformInfo: DetailPlatformInfo?
)