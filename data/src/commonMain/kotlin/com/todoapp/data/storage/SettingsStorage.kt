package com.todoapp.data.storage

expect class SettingsStorage(context: PlatformContext) {
  suspend fun getEnvironment(): String?
  suspend fun setEnvironment(value: String)
  suspend fun getBaseUrl(): String?
  suspend fun setBaseUrl(value: String)
  suspend fun getEmail(): String?
  suspend fun setEmail(value: String?)
}
