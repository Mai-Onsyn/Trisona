package mai_onsn.trisona.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val UnspecifiedTextScheme = TextScheme(
    titleLarge = TextStyle(),
    titleMedium = TextStyle(),
    titleSmall = TextStyle(),
    bodyLarge = TextStyle(),
    bodyMedium = TextStyle(),
    bodySmall = TextStyle(),
    labelLarge = TextStyle(),
    labelMedium = TextStyle(),
    labelSmall = TextStyle(),
    displayLarge = TextStyle(),
)

@Composable
fun BasicTexts(): TextScheme {
    val onSurfaceColor = TrisonaTheme.colorScheme.onSurface
    val onSurfaceVariantColor = TrisonaTheme.colorScheme.onSurfaceVariant
    return TextScheme(
        displayLarge = TextStyle(
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = onSurfaceColor,
            letterSpacing = (-0.25).sp
        ),
        titleLarge = TextStyle(
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            color = onSurfaceColor
        ),
        titleMedium = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = onSurfaceColor,
            letterSpacing = 0.15.sp
        ),
        titleSmall = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = onSurfaceColor,
            letterSpacing = 0.1.sp
        ),
        bodyLarge = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = onSurfaceVariantColor,
            letterSpacing = 0.5.sp
        ),
        bodyMedium = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = onSurfaceVariantColor,
            letterSpacing = 0.25.sp
        ),
        bodySmall = TextStyle(
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = onSurfaceVariantColor,
            letterSpacing = 0.4.sp
        ),
        labelLarge = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = TrisonaTheme.colorScheme.primary,
            letterSpacing = 0.1.sp
        ),
        labelMedium = TextStyle(
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = TrisonaTheme.colorScheme.outline,
            letterSpacing = 0.5.sp
        ),
        labelSmall = TextStyle(
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = TrisonaTheme.colorScheme.outline,
            letterSpacing = 0.5.sp
        )
    )
}