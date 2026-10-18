package com.todoapp.data.util

import kotlinx.datetime.LocalDateTime

internal fun parseLocalDateTime(value: String?): LocalDateTime? {
  if (value.isNullOrBlank()) return null
  return try {
    LocalDateTime.parse(value)
  } catch (_: Throwable) {
    null
  }
}
