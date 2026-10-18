package com.todoapp.data.repository

import com.todoapp.data.api.ApiClient
import com.todoapp.data.api.AuthApi
import com.todoapp.data.dto.AuthResponse
import com.todoapp.data.dto.MessageResponse
import com.todoapp.data.dto.RegisterResponse
import com.todoapp.data.storage.SessionStorage
import com.todoapp.domain.model.Session
import com.todoapp.domain.repository.AuthRepository
import com.todoapp.domain.result.AppResult
import com.todoapp.data.util.safeApiCall
import com.todoapp.data.util.parseLocalDateTime
import io.ktor.client.call.body

class AuthRepositoryImpl(
  private val apiClient: ApiClient,
  private val baseUrlProvider: suspend () -> String,
  private val sessionStorage: SessionStorage
) : AuthRepository {
  private val api = AuthApi(apiClient.httpClient, baseUrlProvider, sessionStorage)

  override suspend fun register(
    email: String,
    password: String,
    confirmPassword: String
  ): AppResult<String> {
    val result = safeApiCall<RegisterResponse> {
      api.register(email, password, confirmPassword)
    }
    return when (result) {
      is AppResult.Success -> AppResult.Success(result.data.userId.orEmpty())
      is AppResult.Failure -> result
    }
  }

  override suspend fun login(email: String, password: String): AppResult<Session> {
    val result = safeApiCall<AuthResponse> {
      api.login(email, password)
    }
    return when (result) {
      is AppResult.Success -> {
        val token = result.data.token.orEmpty()
        val expiresAt = parseLocalDateTime(result.data.expiresAt)
        val session = Session(token = token, expiresAt = expiresAt, email = email)
        saveSession(session)
        AppResult.Success(session)
      }

      is AppResult.Failure -> result
    }
  }

  override suspend fun forgotPassword(email: String): AppResult<String> {
    val result = safeApiCall<MessageResponse> {
      api.forgotPassword(email)
    }
    return when (result) {
      is AppResult.Success -> AppResult.Success(result.data.message.orEmpty())
      is AppResult.Failure -> result
    }
  }

  override suspend fun resetPassword(
    email: String,
    token: String,
    newPassword: String,
    confirmPassword: String
  ): AppResult<String> {
    val result = safeApiCall<MessageResponse> {
      api.resetPassword(email, token, newPassword, confirmPassword)
    }
    return when (result) {
      is AppResult.Success -> AppResult.Success(result.data.message.orEmpty())
      is AppResult.Failure -> result
    }
  }

  override suspend fun getSession(): Session? {
    val token = sessionStorage.getToken() ?: return null
    val expiresAt = parseLocalDateTime(sessionStorage.getExpiresAt())
    val email = sessionStorage.getEmail()
    return Session(token, expiresAt, email)
  }

  override suspend fun saveSession(session: Session) {
    sessionStorage.saveToken(session.token)
    sessionStorage.saveEmail(session.email)
    sessionStorage.saveExpiresAt(session.expiresAt?.toString())
  }

  override suspend fun clearSession() {
    sessionStorage.clear()
  }
}
