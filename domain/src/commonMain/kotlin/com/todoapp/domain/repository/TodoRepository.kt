package com.todoapp.domain.repository

import com.todoapp.domain.model.Todo
import com.todoapp.domain.model.TodoPriority
import com.todoapp.domain.model.TodosFilter
import com.todoapp.domain.result.AppResult
import kotlinx.datetime.LocalDateTime

interface TodoRepository {
  suspend fun getTodos(filter: TodosFilter): AppResult<List<Todo>>
  suspend fun getTodoById(id: String): AppResult<Todo>
  suspend fun createTodo(
    title: String,
    description: String?,
    dueDate: LocalDateTime?,
    priority: TodoPriority
  ): AppResult<Todo>

  suspend fun updateTodo(
    id: String,
    title: String,
    description: String?,
    dueDate: LocalDateTime?,
    priority: TodoPriority,
    isCompleted: Boolean
  ): AppResult<Todo>

  suspend fun toggleTodo(id: String): AppResult<Todo>
  suspend fun deleteTodo(id: String): AppResult<Unit>
}
