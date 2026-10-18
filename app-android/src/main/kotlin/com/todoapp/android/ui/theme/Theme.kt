package com.todoapp.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColors = darkColorScheme(
  primary = BluePrimary,
  secondary = BlueSecondary,
  tertiary = GreenAccent,
  background = DarkBackground,
  surface = DarkSurface,
  onSurface = DarkOnSurface
)

private val LightColors = lightColorScheme(
  primary = BluePrimary,
  secondary = BlueSecondary,
  tertiary = GreenAccent,
  background = NeutralBackground,
  surface = NeutralSurface,
  onSurface = NeutralOnSurface
)

@Composable
fun TodoTheme(
  mode: ThemeMode = ThemeMode.System,
  content: @Composable () -> Unit
) {
  val isDark = when (mode) {
    ThemeMode.System -> isSystemInDarkTheme()
    ThemeMode.Light -> false
    ThemeMode.Dark -> true
  }
  val colors = if (isDark) DarkColors else LightColors
  MaterialTheme(
    colorScheme = colors,
    typography = Typography,
    content = content
  )
}
