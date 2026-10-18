package com.todoapp.domain.repository

import com.todoapp.domain.model.Environment

interface SettingsRepository {
  suspend fun getEnvironment(): Environment
  suspend fun setEnvironment(environment: Environment)
  suspend fun getBaseUrl(): String
  suspend fun setBaseUrl(baseUrl: String)
  suspend fun getEmail(): String?
  suspend fun setEmail(email: String?)
}
