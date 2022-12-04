package com.example.testapplication.MyApplication.compose_themes

import android.provider.CalendarContract.Colors
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)
val Black_3333 = Color(0xFF333333)
val Black_808080 = Color(0xFF808080)
val Gray_F5F5F5 = Color(0xFFF5F5F5)

val Transparent = Color(0x00FFFFFF)

val BackGroundColor = if(isDark) Black else White
val ItemBackGroundColor = if(isDark) Black_3333 else White
val FontColor = if(isDark) White else Black_3333