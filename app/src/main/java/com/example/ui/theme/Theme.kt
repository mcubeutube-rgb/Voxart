package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Violet80,
    onPrimary = Color(0xFF28006E),
    primaryContainer = Color(0xFF4F378B),
    onPrimaryContainer = Color(0xFFEADDFF),
    secondary = Cyan80,
    onSecondary = Color(0xFF003738),
    secondaryContainer = Color(0xFF004F50),
    onSecondaryContainer = Color(0xFF9CF0F0),
    tertiary = Coral80,
    onTertiary = Color(0xFF5C0011),
    tertiaryContainer = Color(0xFF7D1A25),
    onTertiaryContainer = Color(0xFFFFD9DD),
    background = StudioBackgroundDark,
    onBackground = StudioOnSurfaceDark,
    surface = StudioSurfaceDark,
    onSurface = StudioOnSurfaceDark,
    surfaceVariant = StudioSurfaceVariantDark,
    onSurfaceVariant = Color(0xFFCAC4D0)
)

private val LightColorScheme = lightColorScheme(
    primary = Violet40,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEADDFF),
    onPrimaryContainer = Color(0xFF21005D),
    secondary = Cyan40,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFC7F3F3),
    onSecondaryContainer = Color(0xFF002021),
    tertiary = Coral40,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFD9DD),
    onTertiaryContainer = Color(0xFF40000A),
    background = StudioBackgroundLight,
    onBackground = StudioOnSurfaceLight,
    surface = StudioSurfaceLight,
    onSurface = StudioOnSurfaceLight,
    surfaceVariant = StudioSurfaceVariantLight,
    onSurfaceVariant = Color(0xFF49454F)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
