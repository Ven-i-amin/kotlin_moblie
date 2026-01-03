package ru.vsu.task1.domain.usecases

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileUseCase {
    private val _userToken = MutableStateFlow<String?>(null)
    val userToken = _userToken.asStateFlow()
    private val _changeCurrencySymbol = MutableStateFlow("USDT")
    val changeCurrencySymbol = _changeCurrencySymbol.asStateFlow()


    fun setUserToken(token: String) {
        _userToken.value = token
    }
}