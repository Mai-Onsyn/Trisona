package mai_onsyn.trisona.ui.module.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import kotlin.math.roundToInt

@Composable
fun AttachedPopup(
    triggerLayoutRect: IntRect,
    maxWidth: Dp = Dp.Unspecified,
    maxHeight: Dp = Dp.Unspecified,
    minWidth: Dp = 0.dp, // 默认最小值给 0.dp 以防报错
    minHeight: Dp = 0.dp,
    followCursor: Boolean = false,
    cursorOffset: IntOffset = IntOffset(0, 0),
    showProgress: Float = 1f,
    offset: IntOffset = IntOffset.Zero,
    align: Alignment = Alignment.TopStart,
    elevation: Dp = 8.dp, // 暴露阴影参数
    shape: Shape = RoundedCornerShape(8.dp), // 暴露形状参数，graphicsLayer 绘制阴影需要
    boundCheck: Boolean = true,
    content: @Composable () -> Unit
) {
    // 处理跟手偏移逻辑
    val finalOffset = if (followCursor) offset + cursorOffset else offset

    // 使用 remember 避免 Provider 频繁重建
    val popupPositionProvider = remember(finalOffset, align, triggerLayoutRect) {
        AttachedPopupPositionProvider(
            offset = finalOffset,
            align = align,
            otherAnchor = triggerLayoutRect,
            boundCheck = boundCheck
        )
    }

    if (showProgress != 0f) Popup(popupPositionProvider = popupPositionProvider) {
        Box(
            modifier = Modifier
                // 1. 使用 graphicsLayer 统一处理透明度、裁剪和阴影
                // 当 alpha < 1f 时，Compose 会自动启用 ModulateAlpha 合成策略，解决阴影透视 Bug
                .graphicsLayer {
                    alpha = showProgress
                    shadowElevation = elevation.toPx()
                    this.shape = shape
                    clip = true // 将内容裁剪至设定的 shape，避免溢出
                }
                // 2. 自定义 Layout Modifier，根据 showProgress 动态改变容器尺寸
                .animatedPopupSize(
                    showProgress = showProgress,
                    minWidth = minWidth,
                    minHeight = minHeight,
                    maxWidth = maxWidth,
                    maxHeight = maxHeight
                )
        ) {
            content()
        }
    }
}

/**
 * 自定义 Layout Modifier：用于根据 showProgress 动态控制 Popup 的尺寸
 */
fun Modifier.animatedPopupSize(
    showProgress: Float,
    minWidth: Dp,
    minHeight: Dp,
    maxWidth: Dp,
    maxHeight: Dp
) = layout { measurable, constraints ->
    // 如果外部指定了 Max 尺寸，就转换为 Px；如果没有指定 (Unspecified)，则使用可用空间最大值
    val maxW = if (maxWidth.isSpecified) maxWidth.roundToPx() else constraints.maxWidth
    val maxH = if (maxHeight.isSpecified) maxHeight.roundToPx() else constraints.maxHeight

    // 以极其宽松的约束测量 Content，以获取其真实所需尺寸 (Intrinsic Size)
    val placeable = measurable.measure(
        constraints.copy(
            minWidth = 0,
            minHeight = 0,
            maxWidth = maxW,
            maxHeight = maxH
        )
    )

    // 确定动画的终点大小 (Target Size)
    // 如果明确传了 Max，就展开到 Max；否则展开到内容自身测量的真实大小
    val targetW = if (maxWidth.isSpecified) maxW else placeable.width
    val targetH = if (maxHeight.isSpecified) maxH else placeable.height

    // 确定动画的起点大小 (Min Size)
    val minW = if (minWidth.isSpecified) minWidth.roundToPx() else 0
    val minH = if (minHeight.isSpecified) minHeight.roundToPx() else 0

    // 根据 showProgress (0f ~ 1f) 进行线性插值
    val currentW = minW + ((targetW - minW) * showProgress).roundToInt()
    val currentH = minH + ((targetH - minH) * showProgress).roundToInt()

    // 约束布局到插值后的尺寸
    layout(currentW, currentH) {
        // 将内容放置在左上角 (溢出部分已被上层的 graphicsLayer clip=true 裁掉)
        placeable.placeRelative(0, 0)
    }
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
        // 修正了你的代码中变量名同名遮蔽的问题 (anchorBounds = otherAnchor ?: anchorBounds)
        val actualAnchor = otherAnchor ?: anchorBounds

        val alignmentOffset = align.align(
            size = popupContentSize,
            space = IntSize(actualAnchor.width, actualAnchor.height),
            layoutDirection = layoutDirection
        )

        var x = actualAnchor.left + alignmentOffset.x + offset.x
        var y = actualAnchor.top + alignmentOffset.y + offset.y

        if (boundCheck) {
            x = x.coerceIn(0, (windowSize.width - popupContentSize.width).coerceAtLeast(0))
            y = y.coerceIn(0, (windowSize.height - popupContentSize.height).coerceAtLeast(0))
        }

        return IntOffset(x, y)
    }
}