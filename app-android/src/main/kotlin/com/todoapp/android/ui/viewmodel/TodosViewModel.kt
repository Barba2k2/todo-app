package com.todoapp.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todoapp.domain.model.SortBy
import com.todoapp.domain.model.Todo
import com.todoapp.domain.model.TodoPriority
import com.todoapp.domain.model.TodosFilter
import com.todoapp.domain.result.AppResult
import com.todoapp.domain.usecase.DeleteTodoUseCase
import com.todoapp.domain.usecase.GetTodosUseCase
import com.todoapp.domain.usecase.ToggleTodoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodosViewModel @Inject constructor(
    private val getTodosUseCase: GetTodosUseCase,
    private val toggleTodoUseCase: ToggleTodoUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase
) : ViewModel() {
  private val _state = MutableStateFlow(TodosUiState())
  val state: StateFlow<TodosUiState> = _state.asStateFlow()

  fun loadTodos() {
    val filter = _state.value.filter
    _state.value = _state.value.copy(loading = true)
    viewModelScope.launch {
            when (val result = getTodosUseCase(filter)) {
        is AppResult.Success -> _state.value = _state.value.copy(
          loading = false,
          todos = result.data,
          error = null
        )

        is AppResult.Failure -> _state.value = _state.value.copy(
          loading = false,
          error = result.error.message ?: "Erro ao carregar"
        )
      }
    }
  }

  fun updateFilter(
    isCompleted: Boolean?,
    priority: TodoPriority?,
    sortBy: SortBy,
    sortDescending: Boolean
  ) {
    _state.value = _state.value.copy(
      filter = TodosFilter(isCompleted, priority, sortBy, sortDescending)
    )
    loadTodos()
  }

  fun toggleTodo(todo: Todo) {
    viewModelScope.launch {
            val result = toggleTodoUseCase(todo.id)
      if (result is AppResult.Success) {
        loadTodos()
      }
    }
  }

  fun deleteTodo(id: String) {
    viewModelScope.launch {
            val result = deleteTodoUseCase(id)
      if (result is AppResult.Success) {
        loadTodos()
      } else if (result is AppResult.Failure) {
        _state.value = _state.value.copy(error = result.error.message ?: "Erro ao deletar")
      }
    }
  }
}

data class TodosUiState(
  val loading: Boolean = false,
  val todos: List<Todo> = emptyList(),
  val error: String? = null,
  val filter: TodosFilter = TodosFilter()
)
