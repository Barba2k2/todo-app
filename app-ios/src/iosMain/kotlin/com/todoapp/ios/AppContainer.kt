package com.todoapp.ios

import com.todoapp.data.api.ApiClient
import com.todoapp.data.repository.AuthRepositoryImpl
import com.todoapp.data.repository.SettingsRepositoryImpl
import com.todoapp.data.repository.TodoRepositoryImpl
import com.todoapp.data.storage.SessionStorage
import com.todoapp.data.storage.SettingsStorage
import com.todoapp.domain.repository.AuthRepository
import com.todoapp.domain.repository.SettingsRepository
import com.todoapp.domain.repository.TodoRepository
import com.todoapp.domain.usecase.CreateTodoUseCase
import com.todoapp.domain.usecase.DeleteTodoUseCase
import com.todoapp.domain.usecase.ForgotPasswordUseCase
import com.todoapp.domain.usecase.GetSessionUseCase
import com.todoapp.domain.usecase.GetTodoUseCase
import com.todoapp.domain.usecase.GetTodosUseCase
import com.todoapp.domain.usecase.LoginUseCase
import com.todoapp.domain.usecase.LogoutUseCase
import com.todoapp.domain.usecase.RegisterUseCase
import com.todoapp.domain.usecase.ResetPasswordUseCase
import com.todoapp.domain.usecase.ToggleTodoUseCase
import com.todoapp.domain.usecase.UpdateTodoUseCase

class AppContainer {
  private val apiClient = ApiClient()
  private val sessionStorage = SessionStorage(Unit)
  private val settingsStorage = SettingsStorage(Unit)

  val settingsRepository: SettingsRepository = SettingsRepositoryImpl(settingsStorage)
    val authRepository: AuthRepository = AuthRepositoryImpl(
        apiClient = apiClient,
        baseUrlProvider = { settingsRepository.getBaseUrl() },
        sessionStorage = sessionStorage
    )
    val todoRepository: TodoRepository = TodoRepositoryImpl(
        apiClient = apiClient,
        baseUrlProvider = { settingsRepository.getBaseUrl() },
        sessionStorage = sessionStorage
    )

    val loginUseCase = LoginUseCase(authRepository)
    val registerUseCase = RegisterUseCase(authRepository)
    val forgotPasswordUseCase = ForgotPasswordUseCase(authRepository)
    val resetPasswordUseCase = ResetPasswordUseCase(authRepository)
    val logoutUseCase = LogoutUseCase(authRepository)
    val getSessionUseCase = GetSessionUseCase(authRepository)

    val getTodosUseCase = GetTodosUseCase(todoRepository)
    val getTodoUseCase = GetTodoUseCase(todoRepository)
    val createTodoUseCase = CreateTodoUseCase(todoRepository)
    val updateTodoUseCase = UpdateTodoUseCase(todoRepository)
    val toggleTodoUseCase = ToggleTodoUseCase(todoRepository)
    val deleteTodoUseCase = DeleteTodoUseCase(todoRepository)
}
