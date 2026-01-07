package ru.vsu.task1.data.usecases

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.vsu.task1.data.repositories.user.UserRepository

class AuthUseCase() {
    private val _userToken = MutableStateFlow<String?>(null)
    val userToken = _userToken.asStateFlow()
    private val _changeCurrencySymbol = MutableStateFlow("USDT")
    val changeCurrencySymbol = _changeCurrencySymbol.asStateFlow()

    fun setUserToken(token: String) {
        _userToken.value = token
    }

    fun clearUserToken() {
        _userToken.value = null
    }

    fun checkUserToken() : Boolean {
        return _userToken.value != null
    }
}
