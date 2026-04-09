package mai_onsn.trisona.module.util

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.interaction(
    onPress: PointerInputScope.(PointerEvent) -> Unit = {},
    onRelease: PointerInputScope.(PointerEvent) -> Unit = {},
    onClick: PointerInputScope.(PointerEvent) -> Unit = {},
    onHoverEnter: PointerInputScope.(PointerEvent) -> Unit = {},
    onHoverExit: PointerInputScope.(PointerEvent) -> Unit = {},
    onDrag: PointerInputScope.(PointerEvent) -> Unit = {},
    onMove: PointerInputScope.(PointerEvent) -> Unit = {},
    onHoveredChange: (Boolean) -> Unit = {},
    onPressedChange: (Boolean) -> Unit = {},
    pass: PointerEventPass = PointerEventPass.Initial
): Modifier = this.pointerInput(Unit) {
    awaitPointerEventScope {

        var isHoveredNow = false
        var isPressedNow = false
        while (true) {
            val event = awaitPointerEvent(pass)

            when (event.type) {
                // 鼠标悬停进入
                PointerEventType.Enter -> {
                    if (!isHoveredNow) {
                        onHoverEnter(event)
                        onHoveredChange(true)
                    }
                    isHoveredNow = true
                }

                // 鼠标悬停离开
                PointerEventType.Exit -> {
                    if (isHoveredNow) {
                        onHoverExit(event)
                        onHoveredChange(false)
                    }
                    isHoveredNow = false
                }

                // 鼠标/手指按下
                PointerEventType.Press -> {
                    if (!isPressedNow) {
                        onPress(event)
                        onPressedChange(true)
                    }
                    isPressedNow = true
                }

                PointerEventType.Move -> {
                    onMove(event)
                }

                // 鼠标/手指松开
                PointerEventType.Release -> {
                    if (isPressedNow) {
                        onRelease(event)
                        onPressedChange(false)

                        // 如果松开时，坐标依然在当前组件的尺寸范围内，则触发 onClick
                        val position = event.changes.first().position
                        val isWithinBounds = position.x in 0f..size.width.toFloat() &&
                                position.y in 0f..size.height.toFloat()

                        if (isWithinBounds) {
                            onClick(event)

//                            event.changes.forEach { it.consume() }
                        }
                    }
                    isPressedNow = false
                }
            }

            if (isPressedNow) onDrag(event)
        }
    }
}