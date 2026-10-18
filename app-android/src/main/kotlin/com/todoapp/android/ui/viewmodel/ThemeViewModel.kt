package com.todoapp.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.todoapp.android.ui.theme.ThemeMode
import com.todoapp.android.ui.theme.ThemeStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
  private val storage: ThemeStorage
) : ViewModel() {
  private val _mode = MutableStateFlow(storage.getThemeMode())
  val mode: StateFlow<ThemeMode> = _mode.asStateFlow()

  fun setMode(mode: ThemeMode) {
    storage.setThemeMode(mode)
    _mode.value = mode
  }
}
