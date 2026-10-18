package com.todoapp.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
  val email: String? = null,
  val password: String? = null,
  val confirmPassword: String? = null
)

@Serializable
data class LoginRequest(
  val email: String? = null,
  val password: String? = null
)

@Serializable
data class ForgotPasswordRequest(
  val email: String? = null
)

@Serializable
data class ResetPasswordRequest(
  val email: String? = null,
  val token: String? = null,
  val newPassword: String? = null,
  val confirmPassword: String? = null
)

@Serializable
data class AuthResponse(
  val token: String? = null,
  val expiresAt: String? = null
)

@Serializable
data class RegisterResponse(
  val userId: String? = null
)

@Serializable
data class MessageResponse(
  val message: String? = null
)

@Serializable
data class ErrorResponse(
  val errors: List<String>? = null
)

@Serializable
data class ServerErrorResponse(
  val error: String? = null,
  val details: String? = null
)
