package com.todoapp.android.di

import android.content.Context
import com.todoapp.data.api.ApiClient
import com.todoapp.data.repository.AuthRepositoryImpl
import com.todoapp.data.repository.SettingsRepositoryImpl
import com.todoapp.data.repository.TodoRepositoryImpl
import com.todoapp.data.storage.PlatformContext
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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
  @Provides
  @Singleton
  fun provideApiClient(): ApiClient = ApiClient()

  @Provides
  @Singleton
  fun provideSessionStorage(@ApplicationContext context: Context): SessionStorage {
    return SessionStorage(context as PlatformContext)
  }

  @Provides
  @Singleton
  fun provideSettingsStorage(@ApplicationContext context: Context): SettingsStorage {
    return SettingsStorage(context as PlatformContext)
  }

  @Provides
  @Singleton
  fun provideSettingsRepository(storage: SettingsStorage): SettingsRepository {
    return SettingsRepositoryImpl(storage)
  }

  @Provides
  @Singleton
  fun provideAuthRepository(
    apiClient: ApiClient,
    settingsRepository: SettingsRepository,
    sessionStorage: SessionStorage
  ): AuthRepository {
    return AuthRepositoryImpl(
      apiClient = apiClient,
      baseUrlProvider = { settingsRepository.getBaseUrl() },
      sessionStorage = sessionStorage
    )
  }

  @Provides
  @Singleton
    fun provideTodoRepository(
        apiClient: ApiClient,
        settingsRepository: SettingsRepository,
        sessionStorage: SessionStorage
    ): TodoRepository {
        return TodoRepositoryImpl(
            apiClient = apiClient,
            baseUrlProvider = { settingsRepository.getBaseUrl() },
            sessionStorage = sessionStorage
        )
    }

    @Provides
    fun provideLoginUseCase(repository: AuthRepository) = LoginUseCase(repository)

    @Provides
    fun provideRegisterUseCase(repository: AuthRepository) = RegisterUseCase(repository)

    @Provides
    fun provideForgotPasswordUseCase(repository: AuthRepository) = ForgotPasswordUseCase(repository)

    @Provides
    fun provideResetPasswordUseCase(repository: AuthRepository) = ResetPasswordUseCase(repository)

    @Provides
    fun provideLogoutUseCase(repository: AuthRepository) = LogoutUseCase(repository)

    @Provides
    fun provideGetSessionUseCase(repository: AuthRepository) = GetSessionUseCase(repository)

    @Provides
    fun provideGetTodosUseCase(repository: TodoRepository) = GetTodosUseCase(repository)

    @Provides
    fun provideGetTodoUseCase(repository: TodoRepository) = GetTodoUseCase(repository)

    @Provides
    fun provideCreateTodoUseCase(repository: TodoRepository) = CreateTodoUseCase(repository)

    @Provides
    fun provideUpdateTodoUseCase(repository: TodoRepository) = UpdateTodoUseCase(repository)

    @Provides
    fun provideToggleTodoUseCase(repository: TodoRepository) = ToggleTodoUseCase(repository)

    @Provides
    fun provideDeleteTodoUseCase(repository: TodoRepository) = DeleteTodoUseCase(repository)
}
