package mai_onsn.trisona.module.layout

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mai_onsn.trisona.util.interaction
import mai_onsn.trisona.util.tweenSpecFloat
import mai_onsn.trisona.theme.LocalAppTheme
import mai_onsn.trisona.util.appShadow
import kotlin.compareTo

@Composable
fun PopupBox(
    modifier: Modifier = Modifier,
    popupShape: Shape = RectangleShape,
    maxWidth: Dp = Dp.Unspecified,
    maxHeight: Dp = Dp.Unspecified,
    minWidth: Dp = Dp.Unspecified,
    minHeight: Dp = Dp.Unspecified,
    clickTrigger: Boolean = true,
    triggerDelay: Long = 0,
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
    var showPopupAnimate by remember { mutableStateOf(false) }
    var showPopup by remember { mutableStateOf(false) }

    val popupShowProgress by animateFloatAsState(
        targetValue = if (
                if (clickTrigger) showPopupAnimate
                else (isHovered || isPressed) && showPopup
            ) 1f else 0f,
        animationSpec = tweenSpecFloat,
        finishedListener = {
            if (it == 0f) showPopup = false
        }
    )

    val scope = rememberCoroutineScope()
    var delayJob by remember { mutableStateOf<Job?>(null) }
    fun schedulePopupShow() {
        delayJob?.cancel()
        if (triggerDelay <= 0) {
            showPopupAnimate = true
            showPopup = true
        } else {
            delayJob = scope.launch {
                delay(triggerDelay)
                showPopupAnimate = true
                showPopup = true
            }
        }
    }

    var mouseOffset by remember { mutableStateOf(IntOffset.Zero) }

    BoxWithConstraints(
        contentAlignment = interactionContentAlignment,
        modifier = modifier
            .then(
                if (clickTrigger) Modifier.interaction(
                    onPressedChange = { isPressed = it },
                    onHoveredChange = { isHovered = it },
                    onClick = {
                        schedulePopupShow()
                    },
                    onMove = {
                        val offset = it.changes.first().position
                        mouseOffset = IntOffset(offset.x.toInt(), offset.y.toInt())
                    }
                ) else Modifier.interaction(
                    onPressedChange = { isPressed = it },
                    onHoveredChange = { isHovered = it },
                    onHoverEnter = {
                        schedulePopupShow()
                    },
                    onHoverExit = {
                        delayJob?.cancel()
                    },
                    onMove = {
                        val offset = it.changes.first().position
                        mouseOffset = IntOffset(offset.x.toInt(), offset.y.toInt())
                    }
                )
            )
    ) {
        interactionContent()
        if (showPopup) {
            Popup(
                alignment = if (followCursor) Alignment.BottomStart else popupAlignment,
                offset = if (followCursor) {
                    IntOffset(
                        mouseOffset.x + cursorOffset.x,
                        mouseOffset.y + cursorOffset.y
                    )
                } else IntOffset.Zero,
                onDismissRequest = { showPopupAnimate = false },
            ) {
                Box(
                    contentAlignment = popupContentAlignment,
                    modifier = Modifier
                        .appShadow(
                            shadowShape = popupShape,
                            opacity = popupShowProgress,
                        )
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