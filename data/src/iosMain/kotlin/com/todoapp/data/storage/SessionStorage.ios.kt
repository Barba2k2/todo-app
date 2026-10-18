package com.todoapp.data.storage

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.CoreFoundation.CFDictionaryRef
import platform.CoreFoundation.CFTypeRefVar
import platform.CoreFoundation.kCFBooleanTrue
import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.dataUsingEncoding
import platform.Foundation.create
import platform.Security.SecItemAdd
import platform.Security.SecItemCopyMatching
import platform.Security.SecItemDelete
import platform.Security.errSecSuccess
import platform.Security.kSecAttrAccount
import platform.Security.kSecAttrService
import platform.Security.kSecClass
import platform.Security.kSecClassGenericPassword
import platform.Security.kSecMatchLimit
import platform.Security.kSecMatchLimitOne
import platform.Security.kSecReturnData
import platform.Security.kSecValueData

@OptIn(ExperimentalForeignApi::class)
actual class SessionStorage actual constructor(context: PlatformContext) {
  actual suspend fun getToken(): String? = readKeychain(KEY_TOKEN)

  actual suspend fun saveToken(token: String?) {
    writeKeychain(KEY_TOKEN, token)
  }

  actual suspend fun getEmail(): String? = readKeychain(KEY_EMAIL)

  actual suspend fun saveEmail(email: String?) {
    writeKeychain(KEY_EMAIL, email)
  }

  actual suspend fun getExpiresAt(): String? = readKeychain(KEY_EXPIRES_AT)

  actual suspend fun saveExpiresAt(value: String?) {
    writeKeychain(KEY_EXPIRES_AT, value)
  }

  actual suspend fun clear() {
    deleteKeychain(KEY_TOKEN)
    deleteKeychain(KEY_EMAIL)
    deleteKeychain(KEY_EXPIRES_AT)
  }

  private fun keychainQuery(key: String): MutableMap<Any?, Any?> = mutableMapOf(
    kSecClass to kSecClassGenericPassword,
    kSecAttrService to SERVICE,
    kSecAttrAccount to key
  )

  private fun readKeychain(key: String): String? = memScoped {
    val query = keychainQuery(key)
    query[kSecReturnData] = kCFBooleanTrue
    query[kSecMatchLimit] = kSecMatchLimitOne

    val result = alloc<CFTypeRefVar>()
    val status = SecItemCopyMatching(query as CFDictionaryRef, result.ptr)
    if (status != errSecSuccess) return@memScoped null

    val data = result.value as? NSData ?: return@memScoped null
    val string = NSString.create(data, NSUTF8StringEncoding)
    string?.toString()
  }

  private fun writeKeychain(key: String, value: String?) {
    deleteKeychain(key)
    if (value == null) return

    val data = value.toNSData() ?: return
    val attributes = keychainQuery(key)
    attributes[kSecValueData] = data
    SecItemAdd(attributes as CFDictionaryRef, null)
  }

  private fun deleteKeychain(key: String) {
    val query = keychainQuery(key)
    SecItemDelete(query as CFDictionaryRef)
  }

  private fun String.toNSData(): NSData? = (this as NSString).dataUsingEncoding(NSUTF8StringEncoding)

  private companion object {
    const val SERVICE = "com.todoapp.session"
    const val KEY_TOKEN = "token"
    const val KEY_EMAIL = "email"
    const val KEY_EXPIRES_AT = "expiresAt"
  }
}
