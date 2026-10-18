package com.todoapp.data.storage

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

actual class SessionStorage actual constructor(context: PlatformContext) {
  private val prefs = EncryptedSharedPreferences.create(
    context,
    "session_storage",
    MasterKey.Builder(context)
      .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
      .build(),
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
  )

  actual suspend fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

  actual suspend fun saveToken(token: String?) {
    prefs.edit().putString(KEY_TOKEN, token).apply()
  }

  actual suspend fun getEmail(): String? = prefs.getString(KEY_EMAIL, null)

  actual suspend fun saveEmail(email: String?) {
    prefs.edit().putString(KEY_EMAIL, email).apply()
  }

  actual suspend fun getExpiresAt(): String? = prefs.getString(KEY_EXPIRES_AT, null)

  actual suspend fun saveExpiresAt(value: String?) {
    prefs.edit().putString(KEY_EXPIRES_AT, value).apply()
  }

  actual suspend fun clear() {
    prefs.edit().clear().apply()
  }

  private companion object {
    const val KEY_TOKEN = "token"
    const val KEY_EMAIL = "email"
    const val KEY_EXPIRES_AT = "expiresAt"
  }
}
