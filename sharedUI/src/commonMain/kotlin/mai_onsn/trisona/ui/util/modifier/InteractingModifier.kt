package mai_onsn.trisona.ui.util.modifier

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun Modifier.interaction(
    onPress: PointerInputScope.(PointerEvent) -> Unit = {},
    onRelease: PointerInputScope.(PointerEvent) -> Unit = {},
    onClick: PointerInputScope.(PointerEvent) -> Unit = {},
    onDoubleClick: PointerInputScope.(PointerEvent) -> Unit = {},
    onHoverEnter: PointerInputScope.(PointerEvent) -> Unit = {},
    onHoverExit: PointerInputScope.(PointerEvent) -> Unit = {},
    onDrag: PointerInputScope.(PointerEvent) -> Unit = {},
    onMove: PointerInputScope.(PointerEvent) -> Unit = {},
    onScroll: PointerInputScope.(PointerEvent) -> Unit = {},
    onHoveredChange: (Boolean) -> Unit = {},
    onPressedChange: (Boolean) -> Unit = {},
    pass: PointerEventPass = PointerEventPass.Initial
): Modifier {
    val currentOnPress by rememberUpdatedState(onPress)
    val currentOnRelease by rememberUpdatedState(onRelease)
    val currentOnClick by rememberUpdatedState(onClick)
    val currentOnDoubleClick by rememberUpdatedState(onDoubleClick)
    val currentOnHoverEnter by rememberUpdatedState(onHoverEnter)
    val currentOnHoverExit by rememberUpdatedState(onHoverExit)
    val currentOnDrag by rememberUpdatedState(onDrag)
    val currentOnMove by rememberUpdatedState(onMove)
    val currentOnScroll by rememberUpdatedState(onScroll)
    val currentOnHoveredChange by rememberUpdatedState(onHoveredChange)
    val currentOnPressedChange by rememberUpdatedState(onPressedChange)
    return this.pointerInput(Unit) {
        awaitPointerEventScope {

            var isHoveredNow = false
            var isPressedNow = false

            var lastClickTime = System.currentTimeMillis()
            while (true) {
                val event = awaitPointerEvent(pass)

                when (event.type) {
                    // 鼠标悬停进入
                    PointerEventType.Enter -> {
                        if (!isHoveredNow) {
                            currentOnHoverEnter(event)
                            currentOnHoveredChange(true)
                        }
                        isHoveredNow = true
                    }

                    // 鼠标悬停离开
                    PointerEventType.Exit -> {
                        if (isHoveredNow) {
                            currentOnHoverExit(event)
                            currentOnHoveredChange(false)
                        }
                        isHoveredNow = false
                    }

                    // 鼠标/手指按下
                    PointerEventType.Press -> {
                        if (!isPressedNow) {
                            currentOnPress(event)
                            currentOnPressedChange(true)
                        }
                        isPressedNow = true
                    }

                    PointerEventType.Move -> {
                        currentOnMove(event)
                    }

                    PointerEventType.Scroll -> {
                        currentOnScroll(event)
                    }

                    // 鼠标/手指松开
                    PointerEventType.Release -> {
                        if (isPressedNow) {
                            currentOnRelease(event)
                            currentOnPressedChange(false)

                            // 如果松开时，坐标依然在当前组件的尺寸范围内，则触发 onClick
                            val position = event.changes.first().position
                            val isWithinBounds = position.x in 0f..size.width.toFloat() &&
                                    position.y in 0f..size.height.toFloat()

                            if (isWithinBounds) {
                                currentOnClick(event)
                                if (System.currentTimeMillis() - lastClickTime < 300) {
                                    currentOnDoubleClick(event)
                                    lastClickTime = 0
                                } else lastClickTime = System.currentTimeMillis()
                            }
                        }
                        isPressedNow = false
                    }
                }

                if (isPressedNow) currentOnDrag(event)
            }
        }
    }
}