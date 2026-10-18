package com.todoapp.data.repository

import com.todoapp.data.api.ApiClient
import com.todoapp.data.api.TodoApi
import com.todoapp.data.mapper.toDomain
import com.todoapp.data.mapper.toApiValue
import com.todoapp.data.storage.SessionStorage
import com.todoapp.data.util.safeApiCall
import com.todoapp.domain.model.Todo
import com.todoapp.domain.model.TodoPriority
import com.todoapp.domain.model.TodosFilter
import com.todoapp.domain.repository.TodoRepository
import com.todoapp.domain.result.AppResult
import kotlinx.datetime.LocalDateTime

class TodoRepositoryImpl(
  private val apiClient: ApiClient,
  private val baseUrlProvider: suspend () -> String,
  private val sessionStorage: SessionStorage
) : TodoRepository {
  private val api = TodoApi(apiClient.httpClient, baseUrlProvider, sessionStorage)

  override suspend fun getTodos(filter: TodosFilter): AppResult<List<Todo>> {
    val result = safeApiCall<List<com.todoapp.data.dto.TodoDto>> {
      api.getTodos(filter)
    }
    return when (result) {
      is AppResult.Success -> AppResult.Success(result.data.map { it.toDomain() })
      is AppResult.Failure -> result
    }
  }

  override suspend fun getTodoById(id: String): AppResult<Todo> {
    val result = safeApiCall<com.todoapp.data.dto.TodoDto> {
      api.getTodoById(id)
    }
    return when (result) {
      is AppResult.Success -> AppResult.Success(result.data.toDomain())
      is AppResult.Failure -> result
    }
  }

  override suspend fun createTodo(
    title: String,
    description: String?,
    dueDate: LocalDateTime?,
    priority: TodoPriority
  ): AppResult<Todo> {
    val result = safeApiCall<com.todoapp.data.dto.TodoDto> {
      api.createTodo(title, description, dueDate?.toString(), priority.toApiValue())
    }
    return when (result) {
      is AppResult.Success -> AppResult.Success(result.data.toDomain())
      is AppResult.Failure -> result
    }
  }

  override suspend fun updateTodo(
    id: String,
    title: String,
    description: String?,
    dueDate: LocalDateTime?,
    priority: TodoPriority,
    isCompleted: Boolean
  ): AppResult<Todo> {
    val result = safeApiCall<com.todoapp.data.dto.TodoDto> {
      api.updateTodo(id, title, description, dueDate?.toString(), priority.toApiValue(), isCompleted)
    }
    return when (result) {
      is AppResult.Success -> AppResult.Success(result.data.toDomain())
      is AppResult.Failure -> result
    }
  }

  override suspend fun toggleTodo(id: String): AppResult<Todo> {
    val result = safeApiCall<com.todoapp.data.dto.TodoDto> {
      api.toggleTodo(id)
    }
    return when (result) {
      is AppResult.Success -> AppResult.Success(result.data.toDomain())
      is AppResult.Failure -> result
    }
  }

  override suspend fun deleteTodo(id: String): AppResult<Unit> {
    return safeApiCall {
      api.deleteTodo(id)
    }
  }
}
