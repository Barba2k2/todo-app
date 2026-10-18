package com.todoapp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.todoapp.android.ui.TodoApp
import com.todoapp.android.ui.theme.TodoTheme
import com.todoapp.android.ui.viewmodel.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val themeViewModel: ThemeViewModel = androidx.hilt.navigation.compose.hiltViewModel()
      val mode by themeViewModel.mode.collectAsState()
      TodoTheme(mode = mode) {
        TodoApp()
      }
    }
  }
}
