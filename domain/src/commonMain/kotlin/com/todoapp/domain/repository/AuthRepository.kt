package com.todoapp.domain.repository

import com.todoapp.domain.model.Session
import com.todoapp.domain.result.AppResult

interface AuthRepository {
  suspend fun register(email: String, password: String, confirmPassword: String): AppResult<String>
  suspend fun login(email: String, password: String): AppResult<Session>
  suspend fun forgotPassword(email: String): AppResult<String>
  suspend fun resetPassword(
    email: String,
    token: String,
    newPassword: String,
    confirmPassword: String
  ): AppResult<String>

  suspend fun getSession(): Session?
  suspend fun saveSession(session: Session)
  suspend fun clearSession()
}
