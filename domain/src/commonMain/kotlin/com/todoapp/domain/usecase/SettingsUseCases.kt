package com.todoapp.domain.usecase

import com.todoapp.domain.model.Environment
import com.todoapp.domain.repository.SettingsRepository

class GetEnvironmentUseCase(private val repository: SettingsRepository) {
  suspend operator fun invoke() = repository.getEnvironment()
}

class SetEnvironmentUseCase(private val repository: SettingsRepository) {
  suspend operator fun invoke(environment: Environment) = repository.setEnvironment(environment)
}

class GetBaseUrlUseCase(private val repository: SettingsRepository) {
  suspend operator fun invoke() = repository.getBaseUrl()
}

class SetBaseUrlUseCase(private val repository: SettingsRepository) {
  suspend operator fun invoke(baseUrl: String) = repository.setBaseUrl(baseUrl)
}

class GetEmailUseCase(private val repository: SettingsRepository) {
  suspend operator fun invoke() = repository.getEmail()
}

class SetEmailUseCase(private val repository: SettingsRepository) {
  suspend operator fun invoke(email: String?) = repository.setEmail(email)
}
