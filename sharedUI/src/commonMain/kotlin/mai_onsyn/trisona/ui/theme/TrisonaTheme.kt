package mai_onsyn.trisona.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

private val LocalColorScheme = staticCompositionLocalOf { LightColorScheme }
private val LocalShapeScheme = staticCompositionLocalOf { BasicShapes }
private val LocalTextScheme = staticCompositionLocalOf { UnspecifiedTextScheme }

object TrisonaTheme {
    val colorScheme: ColorScheme
        @Composable @ReadOnlyComposable get() = LocalColorScheme.current

    val shapeScheme: ShapeScheme
        @Composable @ReadOnlyComposable get() = LocalShapeScheme.current

    val textScheme: TextScheme
        @Composable @ReadOnlyComposable get() = LocalTextScheme.current

    val PADDING_MINI = 4.dp
    val PADDING_SMALL = 8.dp
    val PADDING_MEDIUM = 12.dp
    val PADDING_LARGE = 16.dp
}

@Composable
fun TrisonaTheme(
    colorScheme: ColorScheme = LightColorScheme,
    shapeScheme: ShapeScheme = BasicShapes,
    content: @Composable () -> Unit
) {
    val color = animatedColorScheme(colorScheme)
    val shape = animatedShapeScheme(shapeScheme)
    val text = BasicTexts()
    CompositionLocalProvider(
        LocalColorScheme provides color,
        LocalShapeScheme provides shape,
        LocalTextScheme provides text
    ) {
        content()
    }
}

@Composable
private fun animatedColorScheme(colorScheme: ColorScheme): ColorScheme {
    val kClass = ColorScheme::class
    val constructor = remember(kClass) { kClass.primaryConstructor!! }
    val properties = remember(kClass) { kClass.memberProperties }

    val args = constructor.parameters.associateWith { parameter ->
        val property = properties.first { it.name == parameter.name }
        val value = property.get(colorScheme)

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
    return transition
}

@Composable
private fun animatedShapeScheme(shapeScheme: ShapeScheme): ShapeScheme {
    val kClass = ShapeScheme::class
    val constructor = remember(kClass) { kClass.primaryConstructor!! }
    val properties = remember(kClass) { kClass.memberProperties }

    val args = constructor.parameters.associateWith { parameter ->
        val property = properties.first { it.name == parameter.name }
        val value = property.get(shapeScheme)

        if (value is Dp) {
            animateDpAsState(
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
    return transition
}