package com.todoapp.domain.usecase

import com.todoapp.domain.model.Todo
import com.todoapp.domain.model.TodoPriority
import com.todoapp.domain.model.TodosFilter
import com.todoapp.domain.repository.TodoRepository
import com.todoapp.domain.result.AppResult
import kotlinx.datetime.LocalDateTime

class GetTodosUseCase(private val repository: TodoRepository) {
  suspend operator fun invoke(filter: TodosFilter): AppResult<List<Todo>> {
    return repository.getTodos(filter)
  }
}

class GetTodoUseCase(private val repository: TodoRepository) {
  suspend operator fun invoke(id: String): AppResult<Todo> {
    return repository.getTodoById(id)
  }
}

class CreateTodoUseCase(private val repository: TodoRepository) {
  suspend operator fun invoke(
    title: String,
    description: String?,
    dueDate: LocalDateTime?,
    priority: TodoPriority
  ): AppResult<Todo> {
    return repository.createTodo(title, description, dueDate, priority)
  }
}

class UpdateTodoUseCase(private val repository: TodoRepository) {
  suspend operator fun invoke(
    id: String,
    title: String,
    description: String?,
    dueDate: LocalDateTime?,
    priority: TodoPriority,
    isCompleted: Boolean
  ): AppResult<Todo> {
    return repository.updateTodo(id, title, description, dueDate, priority, isCompleted)
  }
}

class ToggleTodoUseCase(private val repository: TodoRepository) {
  suspend operator fun invoke(id: String): AppResult<Todo> {
    return repository.toggleTodo(id)
  }
}

class DeleteTodoUseCase(private val repository: TodoRepository) {
  suspend operator fun invoke(id: String): AppResult<Unit> {
    return repository.deleteTodo(id)
  }
}
