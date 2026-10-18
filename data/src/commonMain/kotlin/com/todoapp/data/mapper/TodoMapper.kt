package com.todoapp.data.mapper

import com.todoapp.data.dto.TodoDto
import com.todoapp.data.util.parseLocalDateTime
import com.todoapp.domain.model.Todo
import com.todoapp.domain.model.TodoPriority
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal fun TodoDto.toDomain(): Todo {
  return Todo(
    id = id.orEmpty(),
    title = title.orEmpty(),
    description = description,
    dueDate = parseLocalDateTime(dueDate),
    priority = priority.toPriority(),
    isCompleted = isCompleted,
    completedAt = parseLocalDateTime(completedAt),
        createdAt = parseLocalDateTime(createdAt) ?: parseLocalDateTime(updatedAt) ?: parseLocalDateTime(dueDate)
            ?: Clock.System.now().toLocalDateTime(TimeZone.UTC),
    updatedAt = parseLocalDateTime(updatedAt)
  )
}

internal fun String?.toPriority(): TodoPriority {
  return when (this) {
    "Low" -> TodoPriority.Low
    "Medium" -> TodoPriority.Medium
    "High" -> TodoPriority.High
    "Urgent" -> TodoPriority.Urgent
    else -> TodoPriority.Medium
  }
}

internal fun TodoPriority.toApiValue(): String {
  return when (this) {
    TodoPriority.Low -> "Low"
    TodoPriority.Medium -> "Medium"
    TodoPriority.High -> "High"
    TodoPriority.Urgent -> "Urgent"
  }
}
