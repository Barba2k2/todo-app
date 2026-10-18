package com.todoapp.data.util

import kotlinx.serialization.json.Json

internal val AppJson = Json {
  ignoreUnknownKeys = true
  isLenient = true
  explicitNulls = false
}
