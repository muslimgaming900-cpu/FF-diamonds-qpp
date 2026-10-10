package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val FFDarkColorScheme = darkColorScheme(
    primary = FFOrange,
    onPrimary = FFBackground,
    primaryContainer = FFOrangeDark,
    onPrimaryContainer = FFTextPrimary,
    secondary = FFCyanDiamond,
    onSecondary = FFBackground,
    secondaryContainer = FFCyanDiamondDark,
    onSecondaryContainer = FFTextPrimary,
    tertiary = FFGold,
    onTertiary = FFBackground,
    background = FFBackground,
    onBackground = FFTextPrimary,
    surface = FFSurface,
    onSurface = FFTextPrimary,
    surfaceVariant = FFSurfaceVariant,
    onSurfaceVariant = FFTextSecondary,
    outline = FFBorder,
    error = FFRedError,
    onError = FFTextPrimary
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep branded esports theme consistent
    content: @Composable () -> Unit
) {
    val colorScheme = FFDarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            window?.let {
                it.statusBarColor = FFBackground.toArgb()
                it.navigationBarColor = FFBackground.toArgb()
                WindowCompat.getInsetsController(it, view).apply {
                    isAppearanceLightStatusBars = false
                    isAppearanceLightNavigationBars = false
                }
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
