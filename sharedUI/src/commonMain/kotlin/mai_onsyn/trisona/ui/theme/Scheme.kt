package mai_onsyn.trisona.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

data class ColorScheme(
    val mono: Color,    // 纯白/黑
    val onMono: Color,  // 纯黑/白

    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val onPrimary: Color,
    val onSecondary: Color,
    val onTertiary: Color,

    val surfaceLowest: Color,
    val surfaceLow: Color,
    val surface: Color,
    val surfaceHigh: Color,
    val surfaceHighest: Color,
    val onSurface: Color,
    val onSurfaceVariant: Color,

    val inverseSurface: Color,
    val inverseOnSurface: Color,

    val background: Color,
    val onBackground: Color,

    val error: Color,
    val onError: Color,
    val warn: Color,
    val onWarn: Color,
    val tip: Color,
    val onTip: Color,
    val success: Color,
    val onSuccess: Color,

    val outline: Color,
    val shadow: Color,
    val shadowStained: Color,
    val scrim: Color
)

data class ShapeScheme(
    val corner: Dp,
    val cornerSmall: Dp,
    val cornerLarge: Dp,
)

data class TextScheme(
    val displayLarge: TextStyle,

    val titleLarge: TextStyle,
    val titleMedium: TextStyle,
    val titleSmall: TextStyle,

    val bodyLarge: TextStyle,
    val bodyMedium: TextStyle,
    val bodySmall: TextStyle,

    val labelLarge: TextStyle,
    val labelMedium: TextStyle,
    val labelSmall: TextStyle,
)