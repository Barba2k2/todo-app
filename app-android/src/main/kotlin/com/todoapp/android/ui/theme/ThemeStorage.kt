package com.todoapp.android.ui.theme

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeStorage @Inject constructor(
  @ApplicationContext private val context: Context
) {
  private val prefs = context.getSharedPreferences("theme_storage", Context.MODE_PRIVATE)

  fun getThemeMode(): ThemeMode {
    return when (prefs.getString(KEY_THEME, ThemeMode.System.name)) {
      ThemeMode.Light.name -> ThemeMode.Light
      ThemeMode.Dark.name -> ThemeMode.Dark
      else -> ThemeMode.System
    }
  }

  fun setThemeMode(mode: ThemeMode) {
    prefs.edit().putString(KEY_THEME, mode.name).apply()
  }

  private companion object {
    const val KEY_THEME = "theme_mode"
  }
}
