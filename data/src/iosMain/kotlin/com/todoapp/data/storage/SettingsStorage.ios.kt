package com.todoapp.data.storage

import platform.Foundation.NSUserDefaults

actual class SettingsStorage actual constructor(context: PlatformContext) {
  private val defaults = NSUserDefaults.standardUserDefaults

  actual suspend fun getEnvironment(): String? = defaults.stringForKey(KEY_ENV)

  actual suspend fun setEnvironment(value: String) {
    defaults.setObject(value, KEY_ENV)
  }

  actual suspend fun getBaseUrl(): String? = defaults.stringForKey(KEY_BASE_URL)

  actual suspend fun setBaseUrl(value: String) {
    defaults.setObject(value, KEY_BASE_URL)
  }

  actual suspend fun getEmail(): String? = defaults.stringForKey(KEY_EMAIL)

  actual suspend fun setEmail(value: String?) {
    if (value == null) {
      defaults.removeObjectForKey(KEY_EMAIL)
    } else {
      defaults.setObject(value, KEY_EMAIL)
    }
  }

  private companion object {
    const val KEY_ENV = "environment"
    const val KEY_BASE_URL = "base_url"
    const val KEY_EMAIL = "email"
  }
}
