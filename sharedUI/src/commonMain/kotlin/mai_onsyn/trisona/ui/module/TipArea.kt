package mai_onsyn.trisona.ui.module

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import mai_onsyn.trisona.ui.module.layout.AttachedPopup
import mai_onsyn.trisona.ui.theme.TrisonaTheme
import mai_onsyn.trisona.ui.util.Config.animationBaseRate
import mai_onsyn.trisona.ui.util.toIntOffset
import mai_onsyn.trisona.ui.util.toIntRect
import mai_onsyn.trisona.util.interaction

@Composable
fun TipArea(
    tip: String,
    tipDealy: Int = 500,
    content: @Composable (Modifier) -> Unit
) {
    var hovered by remember { mutableStateOf(false) }
    var pressed by remember { mutableStateOf(false) }
    val popupShowProgress by animateFloatAsState(
        targetValue = if (hovered || pressed) 1f else 0f,
        animationSpec = tween(durationMillis = animationBaseRate, delayMillis = tipDealy)
    )
    var layoutRect by remember { mutableStateOf(IntRect.Zero) }
    var cursorPos by remember { mutableStateOf(IntOffset.Zero) }
    content(
        Modifier
            .interaction(
                onHoveredChange = { hovered = it },
                onMove = { cursorPos = it.changes.first().position.toIntOffset() }
            )
            .onGloballyPositioned {
                layoutRect = it.boundsInWindow().toIntRect()
            }
    )

    AttachedPopup(
        triggerLayoutRect = layoutRect,
        showProgress = popupShowProgress,
        align = Alignment.BottomStart,
        cursorOffset = IntOffset(0, -24),
        cursorPos = cursorPos,
        followCursor = true,
        shape = RoundedCornerShape(TrisonaTheme.shapeScheme.corner)
    ) {
        Box(
            modifier = Modifier
                .background(TrisonaTheme.colorScheme.surface)
                .padding(TrisonaTheme.PADDING_SMALL)
                .interaction(
                    onHoveredChange = { hovered = it },
                    onPressedChange = { pressed = it }
                )
        ) {
            SelectionContainer {
                Text(
                    text = tip,
                    style = TrisonaTheme.textScheme.bodySmall,
                )
            }
        }
    }
}