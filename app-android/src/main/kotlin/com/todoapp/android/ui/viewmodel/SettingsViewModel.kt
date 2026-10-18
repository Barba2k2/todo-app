package com.todoapp.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todoapp.domain.model.Environment
import com.todoapp.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
  private val repository: SettingsRepository
) : ViewModel() {
  private val _state = MutableStateFlow(SettingsState())
  val state: StateFlow<SettingsState> = _state.asStateFlow()

  init {
    refresh()
  }

  fun refresh() {
    viewModelScope.launch {
      val baseUrl = repository.getBaseUrl()
      val env = repository.getEnvironment()
      val email = repository.getEmail()
      _state.value = _state.value.copy(baseUrl = baseUrl, environment = env, email = email)
    }
  }

  fun setBaseUrl(value: String) {
    viewModelScope.launch {
      repository.setBaseUrl(value)
      refresh()
    }
  }

  fun setEnvironment(environment: Environment) {
    viewModelScope.launch {
      repository.setEnvironment(environment)
      refresh()
    }
  }
}

data class SettingsState(
  val baseUrl: String = "",
  val environment: Environment = Environment.Dev,
  val email: String? = null
)
