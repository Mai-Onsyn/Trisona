package mai_onsn.trisona.module.layout

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import mai_onsn.trisona.module.util.interaction
import mai_onsn.trisona.module.util.tweenSpecFloat
import mai_onsn.trisona.theme.LocalAppTheme

@Composable
fun PopupBox(
    modifier: Modifier = Modifier,
    popupShape: Shape = RectangleShape,
    maxWidth: Dp = Dp.Unspecified,
    maxHeight: Dp = Dp.Unspecified,
    minWidth: Dp = Dp.Unspecified,
    minHeight: Dp = Dp.Unspecified,
    followCursor: Boolean = false,
    cursorOffset: IntOffset = IntOffset(0, 0),
    popupAlignment: Alignment = Alignment.BottomCenter,
    popupContentAlignment: Alignment = Alignment.Center,
    interactionContentAlignment: Alignment = Alignment.Center,
    interactionContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val theme = LocalAppTheme.current

    var isHovered by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }
    var showPopup by remember { mutableStateOf(false) }

    val popupShowProgress by animateFloatAsState(
        targetValue = if (isHovered || isPressed) 1f else 0f,
        animationSpec = tweenSpecFloat,
        finishedListener = {
            if (it == 0f) showPopup = false
        }
    )

    var mouseOffset by remember { mutableStateOf(IntOffset.Zero) }

    BoxWithConstraints(
        contentAlignment = interactionContentAlignment,
        modifier = modifier
            .interaction(
                onPressedChange = { isPressed = it },
                onHoveredChange = { isHovered = it },
                onHoverEnter = { showPopup = true },
                onMove = {
                    val offset = it.changes.first().position
                    mouseOffset = IntOffset(offset.x.toInt(), offset.y.toInt())
                },
            )
    ) {
        interactionContent()

        if (showPopup) {
            Popup(
                alignment = if (followCursor) Alignment.TopStart else popupAlignment,
                offset = if (followCursor) {
                    IntOffset(
                        mouseOffset.x + cursorOffset.x,
                        mouseOffset.y + cursorOffset.y
                    )
                } else IntOffset.Zero,
                onDismissRequest = { showPopup = false },
            ) {
                Box(
                    contentAlignment = popupContentAlignment,
                    modifier = Modifier
                        .graphicsLayer {
                            alpha = popupShowProgress
                            shadowElevation = 12.dp.toPx()
                            shape = popupShape
                            clip = false
                            ambientShadowColor = theme.backGroundShadow.copy(0.3f)
                            spotShadowColor = theme.backGroundShadow.copy(0.5f)
                        }
                        .then(
                            if (maxWidth != Dp.Unspecified && minWidth != Dp.Unspecified) {
                                Modifier.width(minWidth + (maxWidth - minWidth) * popupShowProgress)
                            } else if (maxWidth != Dp.Unspecified) {
                                Modifier.widthIn(max = maxWidth)
                            } else Modifier.wrapContentWidth()
                        )
                        .then(
                            if (maxHeight != Dp.Unspecified && minHeight != Dp.Unspecified) {
                                Modifier.height(minHeight + (maxHeight - minHeight) * popupShowProgress)
                            } else if (maxHeight != Dp.Unspecified) {
                                Modifier.heightIn(max = maxHeight)
                            } else Modifier.wrapContentHeight()
                        )
                        .clip(popupShape)
                        .background(theme.popupBaseColor)
                        .interaction(
                            onPressedChange = { isPressed = it },
                            onHoveredChange = { isHovered = it },
                        )
                ) {
                    content()
                }
            }
        }
    }
}