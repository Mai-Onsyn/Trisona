package mai_onsn.trisona.util

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import mai_onsn.trisona.theme.LocalAppTheme
import org.jetbrains.skiko.Cursor


@Composable
fun ClickableScaleButtonEffect(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onDoubleClick: () -> Unit = {},
    scaleAlignment: Alignment = Alignment.Center,
    hoverScale: Float = 1.1f,
    hoverScaleX: Float = hoverScale,
    hoverScaleY: Float = hoverScale,
    pressedScale: Float = 0.9f,
    pressedScaleX: Float = pressedScale,
    pressedScaleY: Float = pressedScale,
    content: @Composable BoxScope.() -> Unit,
) {
    var isHovered by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }

    val scalex by animateFloatAsState(
        if (isHovered && !isPressed) hoverScaleX else if (isPressed) pressedScaleX else 1f
    )
    val scaley by animateFloatAsState(
        if (isHovered && !isPressed) hoverScaleY else if (isPressed) pressedScaleY else 1f
    )

    Box(
        modifier = modifier
            .interaction(
                onHoveredChange = { isHovered = it },
                onPressedChange = { isPressed = it },
                onClick = { onClick() },
                onDoubleClick = { onDoubleClick() }
            )
            .pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)))
            .graphicsLayer {
                scaleX = scalex
                scaleY = scaley
                transformOrigin = scaleAlignment.toTransformOrigin()
            },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}


@Composable
fun ClickableRoundIconButtonScaleEffect(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    backgroundColor: Color? = null,
    content: @Composable BoxScope.() -> Unit
) {
    var isHovered by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        if (isHovered && !isPressed) 1.1f else if (isPressed) 0.9f else 1f
    )
    val defaultColor by animateColorAsState(
        if (isHovered && !isPressed) LocalAppTheme.current.hoverCover
        else if (isPressed) LocalAppTheme.current.pressedCover
        else Color.Transparent
    )

    Box(
        modifier = modifier
            .clip(CircleShape)
            .interaction(
                onHoveredChange = { isHovered = it },
                onPressedChange = { isPressed = it },
                onClick = { onClick() }
            )
            .pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)))
            .background(backgroundColor ?: defaultColor)
            .scale(scale),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

private fun Alignment.toTransformOrigin(): TransformOrigin {
    val pivotX = when (this) {
        Alignment.TopStart, Alignment.CenterStart, Alignment.BottomStart -> 0f
        Alignment.TopEnd, Alignment.CenterEnd, Alignment.BottomEnd -> 1f
        else -> 0.5f // Center 系列
    }
    val pivotY = when (this) {
        Alignment.TopStart, Alignment.TopCenter, Alignment.TopEnd -> 0f
        Alignment.BottomStart, Alignment.BottomCenter, Alignment.BottomEnd -> 1f
        else -> 0.5f // Center 系列
    }
    return TransformOrigin(pivotX, pivotY)
}