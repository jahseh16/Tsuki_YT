package com.zionhuang.music.ui.theme

import android.graphics.Bitmap
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.palette.graphics.Palette

val DefaultThemeColor = Color(0xFF4285F4)

@Composable
fun InnerTuneTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    pureBlack: Boolean = false,
    themeColor: Color = DefaultThemeColor,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val colorScheme = remember(darkTheme, pureBlack, themeColor) {
        if (themeColor == DefaultThemeColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (darkTheme) dynamicDarkColorScheme(context).pureBlack(pureBlack)
            else dynamicLightColorScheme(context)
        } else {
            val baseScheme = if (darkTheme) {
                darkColorScheme(
                    primary = themeColor,
                    primaryContainer = themeColor.copy(alpha = 0.3f),
                    secondary = themeColor,
                    background = Color(0xFF121212),
                    surface = Color(0xFF1E1E1E)
                )
            } else {
                lightColorScheme(
                    primary = themeColor,
                    primaryContainer = themeColor.copy(alpha = 0.1f),
                    secondary = themeColor,
                    background = Color(0xFFFFFFFF),
                    surface = Color(0xFFF5F5F5)
                )
            }
            baseScheme.pureBlack(darkTheme && pureBlack)
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}

fun Bitmap.extractThemeColor(): Color {
    val palette = Palette.from(this).generate()
    val vibrant = palette.getVibrantColor(DefaultThemeColor.toArgb())
    return Color(vibrant)
}

fun ColorScheme.pureBlack(apply: Boolean) =
    if (apply) copy(
        surface = Color.Black,
        background = Color.Black
    ) else this

val ColorSaver = object : Saver<Color, Int> {
    override fun restore(value: Int): Color = Color(value)
    override fun SaverScope.save(value: Color): Int = value.toArgb()
}
