package com.todoapp.data.repository

import com.todoapp.data.storage.SettingsStorage
import com.todoapp.data.util.defaultDevBaseUrl
import com.todoapp.domain.model.Environment
import com.todoapp.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
  private val storage: SettingsStorage
) : SettingsRepository {
  override suspend fun getEnvironment(): Environment {
    return storage.getEnvironment()?.toEnvironment() ?: Environment.Dev
  }

  override suspend fun setEnvironment(environment: Environment) {
    storage.setEnvironment(environment.name)
  }

  override suspend fun getBaseUrl(): String {
    return storage.getBaseUrl() ?: defaultBaseUrl(getEnvironment())
  }

  override suspend fun setBaseUrl(baseUrl: String) {
    storage.setBaseUrl(baseUrl.trim().removeSuffix("/"))
  }

  override suspend fun getEmail(): String? = storage.getEmail()

  override suspend fun setEmail(email: String?) = storage.setEmail(email)
}

private fun String.toEnvironment(): Environment = when (this) {
  Environment.Stage.name -> Environment.Stage
  Environment.Prod.name -> Environment.Prod
  else -> Environment.Dev
}

private fun defaultBaseUrl(environment: Environment): String {
    return when (environment) {
        Environment.Dev -> defaultDevBaseUrl()
        Environment.Stage -> "https://staging.example.com"
        Environment.Prod -> "https://api.example.com"
    }
}
