package com.todoapp.domain.usecase

import com.todoapp.domain.model.Session
import com.todoapp.domain.repository.AuthRepository
import com.todoapp.domain.result.AppResult

class RegisterUseCase(private val repository: AuthRepository) {
  suspend operator fun invoke(email: String, password: String, confirmPassword: String): AppResult<String> {
    return repository.register(email, password, confirmPassword)
  }
}

class LoginUseCase(private val repository: AuthRepository) {
  suspend operator fun invoke(email: String, password: String): AppResult<Session> {
    return repository.login(email, password)
  }
}

class ForgotPasswordUseCase(private val repository: AuthRepository) {
  suspend operator fun invoke(email: String): AppResult<String> {
    return repository.forgotPassword(email)
  }
}

class ResetPasswordUseCase(private val repository: AuthRepository) {
  suspend operator fun invoke(
    email: String,
    token: String,
    newPassword: String,
    confirmPassword: String
  ): AppResult<String> {
    return repository.resetPassword(email, token, newPassword, confirmPassword)
  }
}

class LogoutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() {
        repository.clearSession()
    }
}

class GetSessionUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.getSession()
}
