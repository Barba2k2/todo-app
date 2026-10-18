package com.todoapp.domain.result

sealed interface AppResult<out T> {
  data class Success<T>(val data: T) : AppResult<T>
  data class Failure(val error: AppError) : AppResult<Nothing>
}

sealed interface AppError {
  val message: String?

  data class Network(override val message: String? = null) : AppError
  data class Unauthorized(override val message: String? = null) : AppError
  data class NotFound(override val message: String? = null) : AppError
  data class Validation(
    override val message: String? = null,
    val details: List<String> = emptyList()
  ) : AppError

  data class Unknown(override val message: String? = null) : AppError
}
