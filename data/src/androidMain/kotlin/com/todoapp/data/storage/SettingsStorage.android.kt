package com.todoapp.data.storage

import android.content.Context

actual class SettingsStorage actual constructor(context: PlatformContext) {
  private val prefs = context.getSharedPreferences("settings_storage", Context.MODE_PRIVATE)

  actual suspend fun getEnvironment(): String? = prefs.getString(KEY_ENV, null)

  actual suspend fun setEnvironment(value: String) {
    prefs.edit().putString(KEY_ENV, value).apply()
  }

  actual suspend fun getBaseUrl(): String? = prefs.getString(KEY_BASE_URL, null)

  actual suspend fun setBaseUrl(value: String) {
    prefs.edit().putString(KEY_BASE_URL, value).apply()
  }

  actual suspend fun getEmail(): String? = prefs.getString(KEY_EMAIL, null)

  actual suspend fun setEmail(value: String?) {
    prefs.edit().putString(KEY_EMAIL, value).apply()
  }

  private companion object {
    const val KEY_ENV = "environment"
    const val KEY_BASE_URL = "base_url"
    const val KEY_EMAIL = "email"
  }
}
