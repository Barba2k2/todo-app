package com.todoapp.data.storage

expect class SessionStorage(context: PlatformContext) {
  suspend fun getToken(): String?
  suspend fun saveToken(token: String?)
  suspend fun getEmail(): String?
  suspend fun saveEmail(email: String?)
  suspend fun getExpiresAt(): String?
  suspend fun saveExpiresAt(value: String?)
  suspend fun clear()
}
