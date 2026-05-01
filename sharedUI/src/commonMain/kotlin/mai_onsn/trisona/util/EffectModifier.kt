package mai_onsn.trisona.util

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun Modifier.background(
    baseColor: Color,
    hoverColor: Color,
    pressedColor: Color
): Modifier {
    var hovered by remember { mutableStateOf(false) }
    var pressed by remember { mutableStateOf(false) }
    val color by animateColorAsState(
        targetValue = if (pressed) pressedColor else if (hovered) hoverColor else baseColor,
        animationSpec = tweenSpecColor
    )
    return this
        .interaction(
            onHoveredChange = { hovered = it },
            onPressedChange = { pressed = it }
        )
        .background(color)
}