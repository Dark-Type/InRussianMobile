package com.example.inrussian.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import nekit.corporation.shift_app.ui.theme.Typography


data class ExtraColors(
    val baseBackground: Color,
    val componentBackground: Color,
    val fontCaptive: Color,
    val errorColor: Color,
    val fontInactive: Color,
    val footnote: Color,
    val inactive: Color,
    val secondaryBackground: Color,
    val stroke: Color,
    val vk: Color,
    val yandex: Color,
    val tabColor: Color
)

val LocalExtraColors = staticCompositionLocalOf {
    ExtraColors(
        baseBackground = BaseBackgroundLight,
        componentBackground = ComponentBackgroundLight,
        errorColor = ErrorColor,
        fontCaptive = FontCaptiveLight,
        fontInactive = FontInactiveLight,
        footnote = FootnoteLight,
        inactive = Inactive,
        secondaryBackground = SecondaryBackgroundLight,
        stroke = StrokeLight,
        vk = LightBlue,
        yandex = Black,
        tabColor = TabBackgroundLight
    )
}

@Composable
fun InRussianTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val extraColors = if (darkTheme) {
        ExtraColors(
            baseBackground = BaseBackgroundDark,
            componentBackground = ComponentBackgroundDark,
            errorColor = ErrorColor,
            fontCaptive = FontCaptiveDark,
            fontInactive = FontInactiveDark,
            footnote = FootnoteDark,
            inactive = Inactive,
            secondaryBackground = SecondaryBackgroundDark,
            stroke = StrokeDark,
            vk = LightBlue,
            yandex = Black,
            tabColor = TabBackgroundDark,
        )
    } else {
        ExtraColors(
            baseBackground = BaseBackgroundLight,
            componentBackground = ComponentBackgroundLight,
            errorColor = ErrorColor,
            fontCaptive = FontCaptiveLight,
            fontInactive = FontInactiveLight,
            footnote = FootnoteLight,
            inactive = Inactive,
            secondaryBackground = SecondaryBackgroundLight,
            stroke = StrokeLight,
            vk = LightBlue,
            yandex = Black,
            tabColor = TabBackgroundLight
        )
    }

    CompositionLocalProvider(LocalExtraColors provides extraColors) {
        MaterialTheme(
            colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme(),
            typography = Typography,
            content = content
        )
    }
}