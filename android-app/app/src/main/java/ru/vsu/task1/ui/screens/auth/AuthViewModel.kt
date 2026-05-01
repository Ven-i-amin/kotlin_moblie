package ru.vsu.task1.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import ru.vsu.task1.data.usecases.AuthUseCase
import ru.vsu.task1.data.repositories.user.UserRepository

class AuthViewModel(
    private val repository: UserRepository,
    private val userUseCase: AuthUseCase
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

    private val json = Json { ignoreUnknownKeys = true }

    fun login(username: String, password: String) {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val response = repository.userLogin(username, password)

                _authToken.value = response
                userUseCase.setUserToken(response)
            } catch (e: Exception) {
                _error.value = e.toUserMessage()
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register(username: String, password: String, confirmPassword: String, name: String) {
        _error.value = null

        if (password != confirmPassword) {
            _error.value = "Passwords do not match"
            return
        }

        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = repository.userRegister(username, password, name)
                _authToken.value = response
                userUseCase.setUserToken(response)
            } catch (e: Exception) {
                _error.value = e.toUserMessage()
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun Exception.toUserMessage(): String =
        when (this) {
            is HttpException -> {
                val apiMessage = response()?.errorBody()
                    ?.string()
                    ?.let { body -> runCatching { json.decodeFromString(ApiErrorMessage.serializer(), body).message }.getOrNull() }

                apiMessage ?: "HTTP ${code()}"
            }

            else -> message ?: "Unknown error"
        }

    @Serializable
    private data class ApiErrorMessage(
        val message: String,
    )
}
