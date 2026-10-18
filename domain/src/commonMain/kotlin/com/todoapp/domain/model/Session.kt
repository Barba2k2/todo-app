package com.todoapp.domain.model

import kotlinx.datetime.LocalDateTime

data class Session(
  val token: String,
  val expiresAt: LocalDateTime? = null,
  val email: String? = null
)
