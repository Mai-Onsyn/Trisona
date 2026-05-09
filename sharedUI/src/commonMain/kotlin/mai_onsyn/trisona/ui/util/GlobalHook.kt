package mai_onsyn.trisona.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import mai_onsyn.trisona.util.interaction

object GlobalHook {
    var pointerPos by mutableStateOf(IntOffset.Zero)
        private set

    var windowRect by mutableStateOf(IntRect.Zero)

    internal fun updatePointerPos(offset: IntOffset) {
        pointerPos = offset
    }

    internal fun updateWindowRect(rect: IntRect) {
        windowRect = rect
    }
}


@Composable
fun Modifier.pointerHookListener(): Modifier {
    return this.interaction(
        onMove = {
            GlobalHook.updatePointerPos(it.changes.first().position.toIntOffset())
        }
    )
        .onGloballyPositioned {
            GlobalHook.updateWindowRect(it.boundsInWindow().toIntRect())
        }
}