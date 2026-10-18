package com.todoapp.android.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
  titleLarge = TextStyle(
    fontWeight = FontWeight.SemiBold,
    fontSize = 22.sp,
    lineHeight = 28.sp
  ),
  titleMedium = TextStyle(
    fontWeight = FontWeight.Medium,
    fontSize = 18.sp,
    lineHeight = 24.sp
  ),
  bodyMedium = TextStyle(
    fontSize = 15.sp,
    lineHeight = 21.sp
  ),
  bodySmall = TextStyle(
    fontSize = 13.sp,
    lineHeight = 18.sp
  )
)
