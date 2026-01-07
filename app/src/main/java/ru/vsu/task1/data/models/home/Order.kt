package ru.vsu.task1.data.models.home

data class Order (
    val id: Long,
    val currencyId: String,
    val currencyName: String,
    val type: String,
    val amount: Double,
    val price: Double,
    val status: String,
)