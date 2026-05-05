package mai_onsn.trisona.ui.module.layout

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider

@Composable
fun AttachedPopup(
    layoutRect: IntRect,
    offset: IntOffset = IntOffset.Zero,
    align: Alignment = Alignment.TopStart,
    content: @Composable () -> Unit
) {
    val popupPositionProvider = AttachedPopupPositionProvider(
        offset = offset,
        align = align,
        otherAnchor = layoutRect
    )
    Popup(
        popupPositionProvider = popupPositionProvider,
    ) { content() }
}

class AttachedPopupPositionProvider(
    val offset: IntOffset = IntOffset.Zero,
    val align: Alignment = Alignment.TopStart,
    val boundCheck: Boolean = true,
    val otherAnchor: IntRect? = null
) : PopupPositionProvider {

    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        val anchorBounds = otherAnchor?: anchorBounds

        val alignmentOffset = align.align(
            size = popupContentSize,
            space = IntSize(anchorBounds.width, anchorBounds.height),
            layoutDirection = layoutDirection
        )

        var x = anchorBounds.left + alignmentOffset.x + offset.x
        var y = anchorBounds.top + alignmentOffset.y + offset.y

        if (boundCheck) {
            x = x.coerceIn(0, (windowSize.width - popupContentSize.width).coerceAtLeast(0))
            y = y.coerceIn(0, (windowSize.height - popupContentSize.height).coerceAtLeast(0))
        }

        return IntOffset(x, y)
    }
}