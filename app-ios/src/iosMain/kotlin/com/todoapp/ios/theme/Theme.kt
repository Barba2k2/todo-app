package com.todoapp.ios.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class ThemeMode { System, Light, Dark }

private val BluePrimary = Color(0xFF295CFF)
private val BlueSecondary = Color(0xFF5B7CFF)
private val GreenAccent = Color(0xFF1FBF75)
private val NeutralBackground = Color(0xFFF7F7F8)
private val NeutralSurface = Color(0xFFFFFFFF)
private val NeutralOnSurface = Color(0xFF121212)
private val DarkBackground = Color(0xFF0E0F12)
private val DarkSurface = Color(0xFF17181C)
private val DarkOnSurface = Color(0xFFEDEDED)

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
fun TodoTheme(mode: ThemeMode, content: @Composable () -> Unit) {
  val isDark = when (mode) {
    ThemeMode.System -> isSystemInDarkTheme()
    ThemeMode.Light -> false
    ThemeMode.Dark -> true
  }

  MaterialTheme(colorScheme = if (isDark) DarkColors else LightColors, content = content)
}
