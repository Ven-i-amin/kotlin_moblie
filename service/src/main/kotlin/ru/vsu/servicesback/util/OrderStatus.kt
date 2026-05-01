package ru.vsu.servicesback.util

import com.fasterxml.jackson.annotation.JsonValue

enum class OrderStatus(private val value: String) {
    STARTED("started"),
    PROCESSING("processing"),
    COMPLETED("completed"),
    CANCELLED("cancelled");

    @JsonValue
    fun value(): String = value
}
