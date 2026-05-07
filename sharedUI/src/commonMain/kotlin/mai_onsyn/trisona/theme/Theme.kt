package mai_onsyn.trisona.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

val LocalAppTheme = staticCompositionLocalOf<AppTheme> {
    error("No AppTheme provided!")
}

@Composable
fun ThemeInterface(
    targetTheme: AppTheme,
    content: @Composable () -> Unit
) {
    val kClass = AppTheme::class
    val constructor = remember(kClass) { kClass.primaryConstructor!! }
    val properties = remember(kClass) { kClass.memberProperties }

    val args = constructor.parameters.associateWith { parameter ->
        val property = properties.first { it.name == parameter.name }
        val value = property.get(targetTheme)

        if (value is Color) {
            animateColorAsState(
                targetValue = value,
                animationSpec = tween(durationMillis = 400),
                label = parameter.name ?: ""
            ).value
        } else {
            value
        }
    }

    val transition = remember(args) {
        constructor.callBy(args)
    }

//    val transition = targetTheme.copy(
//        backgroundColor = mkColorAnimate(targetTheme.backgroundColor),
//        backgroundBloom = mkColorAnimate(targetTheme.backgroundBloom)
//    )

    CompositionLocalProvider(LocalAppTheme provides transition) {
        content()
    }
}

data class AppTheme(
    val themeMain: Color,
    val iconMain: Color,
    val backGroundShadow: Color,
    val backgroundColor: Color,
    val backgroundBloom: Color,
    val albumCardBase: Color,
    val textBaseColor: Color,
    val textLightColor: Color, // light = 轻
    val textPromptColor: Color,
    val titleBarBase: Color,
    val titleBarGradient: Color,
    val buttonBaseColor: Color,
    val buttonSelectedBaseColor: Color,
    val hoverCover: Color,
    val pressedCover: Color,
    val hoverBase: Color,
    val pressedBase: Color,
    val controlIconFill: Color,
    val progressBarColor: Color,
    val popupBaseColor: Color,
    val trackColor: Color
)

val LightColorTheme = AppTheme(
    themeMain = AppColors.White,
    iconMain = AppColors.Black,
    backGroundShadow = AppColors.Black,
    backgroundColor = AppColors.LightBackground,
    backgroundBloom = AppColors.LightBloomPink,
    albumCardBase = AppColors.AccentBlueHover,
    textBaseColor = AppColors.LightText,
    textLightColor = Color(0xFF99CCEE),
    textPromptColor = AppColors.TextPrompt,
    titleBarBase = AppColors.LightTitleBar,
    titleBarGradient = AppColors.LightGradient,
    buttonBaseColor = AppColors.LightSearchField,
    hoverCover = AppColors.LightHoverCover,
    pressedCover = Color(0xFF6080A0),
    hoverBase = Color(0xFFC0C0C0),
    pressedBase = Color(0xFF808080),
    controlIconFill = AppColors.LightControlIconFill,
    progressBarColor = AppColors.White,
    trackColor = Color(0xFF363545),
    popupBaseColor = Color(0xFFA0A0FF),
    buttonSelectedBaseColor = Color(40, 60, 90)
)

val DarkColorTheme = AppTheme(
    themeMain = AppColors.Black,
    iconMain = AppColors.White,
    backGroundShadow = AppColors.White,
    backgroundColor = AppColors.DarkBackground,
    backgroundBloom = AppColors.DarkBloomBlue,
    albumCardBase = AppColors.DarkCard,
    textBaseColor = AppColors.DarkText,
    textLightColor = Color(0xFF4488FF),
    textPromptColor = AppColors.TextPrompt,
    titleBarBase = AppColors.DarkTitleBar,
    titleBarGradient = AppColors.DarkGradient,
    buttonBaseColor = AppColors.DarkSearchField,
    hoverCover = AppColors.DarkHoverCover,
    pressedCover = Color(0xFF6080A0),
    hoverBase = Color(0xFFC0C0C0),
    pressedBase = Color(0xFF808080),
    controlIconFill = AppColors.DarkControlIconFill,
    progressBarColor = AppColors.White,
    trackColor = Color(0xFF363545),
    popupBaseColor = Color(0xFF3030A0),
    buttonSelectedBaseColor = Color(73, 110, 166)
)