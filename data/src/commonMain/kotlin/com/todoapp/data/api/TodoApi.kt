package com.todoapp.data.api

import com.todoapp.data.dto.CreateTodoRequest
import com.todoapp.data.dto.UpdateTodoRequest
import com.todoapp.data.storage.SessionStorage
import com.todoapp.domain.model.SortBy
import com.todoapp.domain.model.TodoPriority
import com.todoapp.domain.model.TodosFilter
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

internal class TodoApi(
  private val client: HttpClient,
  private val baseUrlProvider: suspend () -> String,
  private val sessionStorage: SessionStorage
) {
  private suspend fun authHeader() = sessionStorage.getToken()?.let { "Bearer $it" }

  suspend fun getTodos(filter: TodosFilter): HttpResponse {
    return client.get("${baseUrlProvider()}/api/Todos") {
      authHeader()?.let { header("Authorization", it) }
      filter.isCompleted?.let { parameter("isCompleted", it) }
      filter.priority?.let { parameter("priority", it.toApiValue()) }
      parameter("sortBy", filter.sortBy.toApiValue())
      parameter("sortDescending", filter.sortDescending)
    }
  }

  suspend fun getTodoById(id: String): HttpResponse {
    return client.get("${baseUrlProvider()}/api/Todos/$id") {
      authHeader()?.let { header("Authorization", it) }
    }
  }

  suspend fun createTodo(
    title: String,
    description: String?,
    dueDate: String?,
    priority: String?
  ): HttpResponse {
    return client.post("${baseUrlProvider()}/api/Todos") {
      authHeader()?.let { header("Authorization", it) }
      contentType(ContentType.Application.Json)
      setBody(CreateTodoRequest(title, description, dueDate, priority))
    }
  }

  suspend fun updateTodo(
    id: String,
    title: String,
    description: String?,
    dueDate: String?,
    priority: String?,
    isCompleted: Boolean
  ): HttpResponse {
    return client.put("${baseUrlProvider()}/api/Todos/$id") {
      authHeader()?.let { header("Authorization", it) }
      contentType(ContentType.Application.Json)
      setBody(UpdateTodoRequest(title, description, dueDate, priority, isCompleted))
    }
  }

  suspend fun toggleTodo(id: String): HttpResponse {
    return client.patch("${baseUrlProvider()}/api/Todos/$id/toggle") {
      authHeader()?.let { header("Authorization", it) }
    }
  }

  suspend fun deleteTodo(id: String): HttpResponse {
    return client.delete("${baseUrlProvider()}/api/Todos/$id") {
      authHeader()?.let { header("Authorization", it) }
    }
  }
}

private fun SortBy.toApiValue(): String {
  return when (this) {
    SortBy.Title -> "title"
    SortBy.DueDate -> "dueDate"
    SortBy.Priority -> "priority"
    SortBy.CreatedAt -> "createdAt"
  }
}

private fun TodoPriority.toApiValue(): String {
  return when (this) {
    TodoPriority.Low -> "Low"
    TodoPriority.Medium -> "Medium"
    TodoPriority.High -> "High"
    TodoPriority.Urgent -> "Urgent"
  }
}
