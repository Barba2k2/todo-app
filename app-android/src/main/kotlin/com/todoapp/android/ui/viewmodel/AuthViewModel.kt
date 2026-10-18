package com.todoapp.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todoapp.domain.repository.SettingsRepository
import com.todoapp.domain.result.AppResult
import com.todoapp.domain.usecase.ForgotPasswordUseCase
import com.todoapp.domain.usecase.GetSessionUseCase
import com.todoapp.domain.usecase.LoginUseCase
import com.todoapp.domain.usecase.LogoutUseCase
import com.todoapp.domain.usecase.RegisterUseCase
import com.todoapp.domain.usecase.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getSessionUseCase: GetSessionUseCase,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

  private val _state = MutableStateFlow(AuthUiState())
  val state: StateFlow<AuthUiState> = _state.asStateFlow()

    suspend fun hasSession(): Boolean = getSessionUseCase() != null

  fun login(email: String, password: String) {
    updateState(loading = true)
    viewModelScope.launch {
            when (val result = loginUseCase(email, password)) {
        is AppResult.Success -> {
          settingsRepository.setEmail(email)
          updateState(loading = false, message = "Login efetuado")
        }

        is AppResult.Failure -> {
          updateState(loading = false, error = result.error.message ?: "Falha no login")
        }
      }
    }
  }

  fun register(email: String, password: String, confirm: String) {
    updateState(loading = true)
    viewModelScope.launch {
            when (val result = registerUseCase(email, password, confirm)) {
        is AppResult.Success -> {
          updateState(loading = false, message = "Conta criada")
        }

        is AppResult.Failure -> {
          updateState(loading = false, error = result.error.message ?: "Falha no cadastro")
        }
      }
    }
  }

  fun forgotPassword(email: String) {
    updateState(loading = true)
    viewModelScope.launch {
            when (val result = forgotPasswordUseCase(email)) {
        is AppResult.Success -> updateState(loading = false, message = result.data)
        is AppResult.Failure -> updateState(loading = false, error = result.error.message ?: "Falha ao enviar")
      }
    }
  }

  fun resetPassword(email: String, token: String, newPassword: String, confirm: String) {
    updateState(loading = true)
    viewModelScope.launch {
            when (val result = resetPasswordUseCase(email, token, newPassword, confirm)) {
        is AppResult.Success -> updateState(loading = false, message = result.data)
        is AppResult.Failure -> updateState(loading = false, error = result.error.message ?: "Falha ao resetar")
      }
    }
  }

  fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            settingsRepository.setEmail(null)
        }
    }

  fun clearMessage() {
    _state.value = _state.value.copy(message = null, error = null)
  }

  private fun updateState(loading: Boolean, message: String? = null, error: String? = null) {
    _state.value = _state.value.copy(loading = loading, message = message, error = error)
  }
}

data class AuthUiState(
  val loading: Boolean = false,
  val message: String? = null,
  val error: String? = null
)
