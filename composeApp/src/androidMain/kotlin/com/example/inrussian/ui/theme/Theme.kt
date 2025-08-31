package nekit.corporation.shift_app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.example.inrussian.ui.theme.BaseBackgroundDark
import com.example.inrussian.ui.theme.BaseBackgroundLight
import com.example.inrussian.ui.theme.Black
import com.example.inrussian.ui.theme.ComponentBackgroundDark
import com.example.inrussian.ui.theme.ComponentBackgroundLight
import com.example.inrussian.ui.theme.FontCaptiveDark
import com.example.inrussian.ui.theme.FontCaptiveLight
import com.example.inrussian.ui.theme.FontInactiveDark
import com.example.inrussian.ui.theme.FontInactiveLight
import com.example.inrussian.ui.theme.FootnoteDark
import com.example.inrussian.ui.theme.FootnoteLight
import com.example.inrussian.ui.theme.Inactive
import com.example.inrussian.ui.theme.LightBlue
import com.example.inrussian.ui.theme.SecondaryBackgroundDark
import com.example.inrussian.ui.theme.SecondaryBackgroundLight
import com.example.inrussian.ui.theme.StrokeDark
import com.example.inrussian.ui.theme.StrokeLight


data class ExtraColors(
    val baseBackground: Color,
    val componentBackground: Color,
    val fontCaptive: Color,
    val fontInactive: Color,
    val footnote: Color,
    val inactive: Color,
    val secondaryBackground: Color,
    val stroke: Color,
    val vk: Color,
    val yandex: Color
)

val LocalExtraColors = staticCompositionLocalOf {
    ExtraColors(
        baseBackground = BaseBackgroundLight,
        componentBackground = ComponentBackgroundLight,
        fontCaptive = FontCaptiveLight,
        fontInactive = FontInactiveLight,
        footnote = FootnoteLight,
        inactive = Inactive,
        secondaryBackground = SecondaryBackgroundLight,
        stroke = StrokeLight,
        vk = LightBlue,
        yandex = Black
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
            fontCaptive = FontCaptiveDark,
            fontInactive = FontInactiveDark,
            footnote = FootnoteDark,
            inactive = Inactive,
            secondaryBackground = SecondaryBackgroundDark,
            stroke = StrokeDark,
            vk = LightBlue,
            yandex = Black
        )
    } else {
        ExtraColors(
            baseBackground = BaseBackgroundLight,
            componentBackground = ComponentBackgroundLight,
            fontCaptive = FontCaptiveLight,
            fontInactive = FontInactiveLight,
            footnote = FootnoteLight,
            inactive = Inactive,
            secondaryBackground = SecondaryBackgroundLight,
            stroke = StrokeLight,
            vk = LightBlue,
            yandex = Black
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