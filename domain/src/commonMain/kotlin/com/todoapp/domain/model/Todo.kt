package com.todoapp.domain.model

import kotlinx.datetime.LocalDateTime

enum class TodoPriority {
  Low,
  Medium,
  High,
  Urgent
}

enum class SortBy {
  Title,
  DueDate,
  Priority,
  CreatedAt
}

data class TodosFilter(
  val isCompleted: Boolean? = null,
  val priority: TodoPriority? = null,
  val sortBy: SortBy = SortBy.CreatedAt,
  val sortDescending: Boolean = false
)

data class Todo(
  val id: String,
  val title: String,
  val description: String? = null,
  val dueDate: LocalDateTime? = null,
  val priority: TodoPriority = TodoPriority.Medium,
  val isCompleted: Boolean = false,
  val completedAt: LocalDateTime? = null,
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime? = null
)
