package ru.vsu.task1.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.vsu.task1.domain.usecases.ProfileUseCase
import ru.vsu.task1.data.repository.auth.AuthRepository

class AuthViewModel(
    private val repository: AuthRepository,
    private val userUseCase: ProfileUseCase
) : ViewModel() {
    // loading
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    // error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    // auth
    private val _authToken = MutableStateFlow("")
    val authToken = _authToken.asStateFlow()

    fun login(username: String, password: String) {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val response = repository.userLogin(username, password)

                _authToken.value = response
                userUseCase.setUserToken(response)

                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message.toString()
                e.printStackTrace()
            }
        }
    }

    fun register(username: String, password: String, confirmPassword: String, name: String) {
        _isLoading.value = true
        _error.value = null

        if (password != confirmPassword) {
            _error.value = "Passwords do not match"
            return
        }

        viewModelScope.launch {
            try {
                val response = repository.userRegister(username, password, name)
                _authToken.value = response
                userUseCase.setUserToken(response)

                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message.toString()
                e.printStackTrace()
            }
        }
    }

}