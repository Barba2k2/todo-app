package com.todoapp.data.api

import com.todoapp.data.dto.ForgotPasswordRequest
import com.todoapp.data.dto.LoginRequest
import com.todoapp.data.dto.RegisterRequest
import com.todoapp.data.dto.ResetPasswordRequest
import com.todoapp.data.storage.SessionStorage
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

internal class AuthApi(
  private val client: HttpClient,
  private val baseUrlProvider: suspend () -> String,
  private val sessionStorage: SessionStorage
) {
  suspend fun register(email: String, password: String, confirmPassword: String): HttpResponse {
    return client.post("${baseUrlProvider()}/api/Auth/register") {
      contentType(ContentType.Application.Json)
      setBody(RegisterRequest(email, password, confirmPassword))
    }
  }

  suspend fun login(email: String, password: String): HttpResponse {
    return client.post("${baseUrlProvider()}/api/Auth/login") {
      contentType(ContentType.Application.Json)
      setBody(LoginRequest(email, password))
    }
  }

  suspend fun forgotPassword(email: String): HttpResponse {
    return client.post("${baseUrlProvider()}/api/Auth/forgot-password") {
      contentType(ContentType.Application.Json)
      setBody(ForgotPasswordRequest(email))
    }
  }

  suspend fun resetPassword(
    email: String,
    token: String,
    newPassword: String,
    confirmPassword: String
  ): HttpResponse {
    return client.post("${baseUrlProvider()}/api/Auth/reset-password") {
      contentType(ContentType.Application.Json)
      setBody(ResetPasswordRequest(email, token, newPassword, confirmPassword))
    }
  }

  suspend fun authHeader(): String? {
    val token = sessionStorage.getToken()
    return token?.let { "Bearer $it" }
  }
}
