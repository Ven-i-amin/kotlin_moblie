package ru.vsu.task1.domain.usecases

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserUseCase {
    private val _userToken = MutableStateFlow<String?>(null)
    val userToken = _userToken.asStateFlow()

    fun setUserToken(token: String?) {
        _userToken.value = token
    }
}