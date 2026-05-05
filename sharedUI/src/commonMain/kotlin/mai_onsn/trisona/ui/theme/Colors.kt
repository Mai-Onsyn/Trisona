package mai_onsn.trisona.ui.theme

import androidx.compose.ui.graphics.Color

val Blue = Color(0xFF1281FF)

val LightColorScheme = ColorScheme(
    mono = Color(0xFFFFFFFF),
    onMono = Color(0xFF000000),

    primary = Blue,
    secondary = Color(0xFF5A91CC),
    tertiary = Color(0xFF8193A5),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFFFFFFF),
    onTertiary = Color(0xFFFFFFFF),

    surfaceLowest = Color(0xFFB8CDE8),     // 较明显的淡蓝灰（最深）
    surfaceLow   = Color(0xFFD0E0F0),     // 中等偏深蓝
    surface      = Color(0xFFE5EFF8),     // 基准淡蓝（略深于常见基准）
    surfaceHigh  = Color(0xFFF2F6FC),     // 很浅淡蓝白
    surfaceHighest= Color(0xFFFFFFFF),    // 纯白（最亮）
    onSurface     = Color(0xFF1A2C3C),    // 深蓝灰，保持高对比
    onSurfaceVariant = Color(0xFF5A6F82),  // 稍浅一些，依然清晰

    inverseSurface = Color(0xFF2F3033),
    inverseOnSurface = Color(0xFFF1F0F4),

    background = Color(188, 218, 249),
    onBackground = Color(0xFF1A1C1E),

    error = Color(0xFF1A1A),
    onError = Color(0xFFFFFFFF),
    warn = Color(0xFFFFB800),
    onWarn = Color(0xFFFFFFFF),
    tip = Color(0xFFFF6B9D),
    onTip = Color(0xFFFFFFFF),
    success = Color(0xFF32CD32),
    onSuccess = Color(0xFFFFFFFF),

    outline = Color(0xFFABAEB3),
    shadow = Color(0xFF000000),
    shadowStained = Blue,
    scrim = Color(0xFF000000).copy(alpha = 0.3f)
)

val DarkColorScheme = ColorScheme(
    mono = Color(0xFF000000),
    onMono = Color(0xFFFFFFFF),

    primary = Color(0xFF75C8FF),
    secondary = Color(0xFF8EACCD),
    tertiary = Color(0xFFA5B4C4),
    onPrimary = Color(0xFF003258),
    onSecondary = Color(0xFF003258),
    onTertiary = Color(0xFF003258),

    surfaceLowest = Color(0xFF0E1012),
    surfaceLow = Color(0xFF171B1F),
    surface = Color(0xFF1B1D1F),
    surfaceHigh = Color(0xFF282A2D),
    surfaceHighest = Color(0xFF333538),
    onSurface = Color(0xFFE2E2E6),
    onSurfaceVariant = Color(0xFFC4C7CC),

    inverseSurface = Color(0xFFE2E2E6),
    inverseOnSurface = Color(0xFF2F3033),

    background = Color(0xFF121314),
    onBackground = Color(0xFFE2E2E6),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    warn = Color(0xFFFFD54F),
    onWarn = Color(0xFF3E2D00),
    tip = Color(0xFFFF8AAB), // 稍亮的粉色
    onTip = Color(0xFF5F0024),
    success = Color(0xFF81C784),
    onSuccess = Color(0xFF00390A),

    outline = Color(0xFF8E9194),
    shadow = Color(0xFF000000),
    shadowStained = Color(0xFF000000),
    scrim = Color(0xFF000000).copy(alpha = 0.5f)
)