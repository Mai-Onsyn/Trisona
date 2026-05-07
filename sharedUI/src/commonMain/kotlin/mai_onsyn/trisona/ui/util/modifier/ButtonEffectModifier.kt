package mai_onsyn.trisona.ui.util.modifier

import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import mai_onsyn.trisona.ui.theme.TrisonaTheme
import mai_onsyn.trisona.ui.util.tweenSpecColor
import org.jetbrains.skiko.Cursor

@Composable
fun Modifier.buttonEffect(
    color: Color = TrisonaTheme.colorScheme.scrim,
    hoverAlpha: Float = 0.2f,
    pressedAlpha: Float = 0.3f
): Modifier {
    var isHovered by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }

    val bgColor by animateColorAsState(
        targetValue =
            if (isPressed) color.copy(pressedAlpha)
            else if (isHovered) color.copy(hoverAlpha)
            else color.copy(0f),
        animationSpec = tweenSpecColor
    )
    return this
        .pointerHoverIcon(PointerIcon.Hand)
        .interaction(
            onHoveredChange = { isHovered = it },
            onPressedChange = { isPressed = it }
        )
        .drawWithContent {
            drawContent()
            drawRect(bgColor)
    }
}