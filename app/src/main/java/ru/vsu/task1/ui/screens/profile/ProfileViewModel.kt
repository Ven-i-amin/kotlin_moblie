package ru.vsu.task1.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.vsu.task1.data.models.auth.User
import ru.vsu.task1.data.usecases.AuthUseCase
import ru.vsu.task1.data.usecases.UserUseCase

class ProfileViewModel(
    private val authUseCase: AuthUseCase,
    private val userUseCase: UserUseCase
) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun fetchUserInfo() {
        val token = authUseCase.userToken.value
        if (token.isNullOrBlank()) {
            _error.value = "Not authenticated"
            return
        }

        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                _user.value = userUseCase.getUserInfo(token)
            } catch (e: Exception) {
                _error.value = "Failed to load profile"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        authUseCase.clearUserToken()
    }

    fun updateUserField(field: ProfileField, value: String) {
        val token = authUseCase.userToken.value
        val current = _user.value
        if (token.isNullOrBlank() || current == null) {
            _error.value = "Not authenticated"
            return
        }

        val updatedUser = when (field) {
            ProfileField.Name -> current.copy(fullName = value)
            ProfileField.Email -> current.copy(email = value)
            ProfileField.Password -> current.copy(password = value)
        }

        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                _user.value = userUseCase.setUserInfo(token, updatedUser)
            } catch (e: Exception) {
                _error.value = "Failed to update profile"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}

enum class ProfileField {
    Name,
    Email,
    Password
}
