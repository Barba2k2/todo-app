package com.todoapp.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todoapp.domain.model.Todo
import com.todoapp.domain.model.TodoPriority
import com.todoapp.domain.result.AppResult
import com.todoapp.domain.usecase.CreateTodoUseCase
import com.todoapp.domain.usecase.DeleteTodoUseCase
import com.todoapp.domain.usecase.GetTodoUseCase
import com.todoapp.domain.usecase.UpdateTodoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TodoEditorViewModel @Inject constructor(
    private val getTodoUseCase: GetTodoUseCase,
    private val createTodoUseCase: CreateTodoUseCase,
    private val updateTodoUseCase: UpdateTodoUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase
) : ViewModel() {
  private val _state = MutableStateFlow(TodoEditorState())
  val state: StateFlow<TodoEditorState> = _state.asStateFlow()

  fun loadTodo(id: String) {
    _state.value = _state.value.copy(loading = true)
    viewModelScope.launch {
            when (val result = getTodoUseCase(id)) {
        is AppResult.Success -> _state.value = _state.value.copy(loading = false, todo = result.data)
        is AppResult.Failure -> _state.value = _state.value.copy(loading = false, error = result.error.message)
      }
    }
  }

  fun createTodo(
    title: String,
    description: String?,
    dueDate: LocalDateTime?,
    priority: TodoPriority
  ) {
    _state.value = _state.value.copy(loading = true)
    viewModelScope.launch {
            when (val result = createTodoUseCase(title, description, dueDate, priority)) {
        is AppResult.Success -> _state.value = _state.value.copy(loading = false, saved = true, todo = result.data)
        is AppResult.Failure -> _state.value = _state.value.copy(loading = false, error = result.error.message)
      }
    }
  }

  fun updateTodo(
    id: String,
    title: String,
    description: String?,
    dueDate: LocalDateTime?,
    priority: TodoPriority,
    isCompleted: Boolean
  ) {
    _state.value = _state.value.copy(loading = true)
    viewModelScope.launch {
            when (val result = updateTodoUseCase(id, title, description, dueDate, priority, isCompleted)) {
        is AppResult.Success -> _state.value = _state.value.copy(loading = false, saved = true, todo = result.data)
        is AppResult.Failure -> _state.value = _state.value.copy(loading = false, error = result.error.message)
      }
    }
  }

  fun deleteTodo(id: String) {
    _state.value = _state.value.copy(loading = true)
    viewModelScope.launch {
            when (val result = deleteTodoUseCase(id)) {
        is AppResult.Success -> _state.value = _state.value.copy(loading = false, saved = true)
        is AppResult.Failure -> _state.value = _state.value.copy(loading = false, error = result.error.message)
      }
    }
  }

  fun clearMessage() {
    _state.value = _state.value.copy(error = null, saved = false)
  }
}

data class TodoEditorState(
  val loading: Boolean = false,
  val todo: Todo? = null,
  val saved: Boolean = false,
  val error: String? = null
)
