package com.todoapp.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TodoDto(
  val id: String? = null,
  val title: String? = null,
  val description: String? = null,
  val dueDate: String? = null,
  val priority: String? = null,
  val isCompleted: Boolean = false,
  val completedAt: String? = null,
  val createdAt: String? = null,
  val updatedAt: String? = null
)

@Serializable
data class CreateTodoRequest(
  val title: String? = null,
  val description: String? = null,
  val dueDate: String? = null,
  val priority: String? = null
)

@Serializable
data class UpdateTodoRequest(
  val title: String? = null,
  val description: String? = null,
  val dueDate: String? = null,
  val priority: String? = null,
  val isCompleted: Boolean = false
)
