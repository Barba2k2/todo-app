package com.todoapp.data.util

import com.todoapp.data.dto.ErrorResponse
import com.todoapp.data.dto.ServerErrorResponse
import com.todoapp.domain.result.AppError
import com.todoapp.domain.result.AppResult
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.SerializationException

internal suspend inline fun <reified T> safeApiCall(
  crossinline call: suspend () -> HttpResponse
): AppResult<T> {
  return try {
    val response = call()
    val status = response.status
    when {
      status.value in 200..299 -> {
        if (status == HttpStatusCode.NoContent || T::class == Unit::class) {
          @Suppress("UNCHECKED_CAST")
          AppResult.Success(Unit as T)
        } else {
          AppResult.Success(response.body())
        }
      }

      status == HttpStatusCode.BadRequest -> {
        val error = parseError(response)
        AppResult.Failure(AppError.Validation(error.message, error.details))
      }

      status == HttpStatusCode.Unauthorized -> {
        val error = parseError(response)
        AppResult.Failure(AppError.Unauthorized(error.message))
      }

      status == HttpStatusCode.NotFound -> {
        val error = parseError(response)
        AppResult.Failure(AppError.NotFound(error.message))
      }

      else -> {
        val error = parseServerError(response)
        AppResult.Failure(AppError.Unknown(error))
      }
    }
  } catch (e: Exception) {
    AppResult.Failure(AppError.Network(e.message))
  }
}

internal suspend fun parseError(response: HttpResponse): ErrorPayload {
  return try {
    val body = response.body<ErrorResponse>()
    val message = body.errors?.firstOrNull()
    ErrorPayload(message, body.errors.orEmpty())
  } catch (_: SerializationException) {
    ErrorPayload(null, emptyList())
  }
}

internal suspend fun parseServerError(response: HttpResponse): String? {
  return try {
    val body = response.body<ServerErrorResponse>()
    body.error ?: body.details
  } catch (_: SerializationException) {
    null
  }
}

internal data class ErrorPayload(
  val message: String?,
  val details: List<String>
)
