package mai_onsn.trisona.module.play_bar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.toPath
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import mai_onsn.trisona.theme.LocalAppTheme

/**
 * Returns a onClick listener that will morph the button into a play button when clicked.
 * Must be used in a layout that combines the play and pause button.
 * 返回组件的点击回调。执行时，将切换播放按钮UI的视觉状态
 * 必须在父容器被点击时调用
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MorphingPlayPauseButton (
    modifier: Modifier = Modifier,
): (Boolean) -> Unit {
    val theme = LocalAppTheme.current

    var pausing by remember { mutableStateOf(true) }
    val animateProgress by animateFloatAsState(if (pausing) 0f else 1f)

    val onClick: (Boolean) -> Unit = { pausing = it }
    Box(
        modifier = modifier
//            .interaction(onClick = onClick)
            .drawWithCache {
                val w = size.width
                val h = size.height

                val halfW = w / 2f
                val halfH = h / 2f
                val radius = size.minDimension / 2f

//                val offset = radius * 0.25f - 1f
                val offset = 0f

                val lTriangle = RoundedPolygon(
                    numVertices = 3,
                    radius = radius,
                    centerX = halfW - offset,
                    centerY = halfH,
                    rounding = CornerRounding(radius / 5)
                )
                val rTriangle = RoundedPolygon(
                    numVertices = 3,
                    radius = radius,
                    centerX = halfW - offset,
                    centerY = halfH,
                    rounding = CornerRounding(radius / 5)
                )

                val spacerHalfWidth = 4f
                val borderSpacerWidth = 5f
                val borderSpacerHeight = 5f
                val lSquare = RoundedPolygon(
                    vertices = floatArrayOf(
                        borderSpacerWidth, borderSpacerHeight,
                        halfW - spacerHalfWidth, borderSpacerHeight,
                        halfW - spacerHalfWidth, h - borderSpacerHeight,
                        borderSpacerWidth, h - borderSpacerHeight
                    ),
                    rounding = CornerRounding(radius / 5)
                )
                val rSquare = RoundedPolygon(
                    vertices = floatArrayOf(
                        halfW + spacerHalfWidth, borderSpacerHeight,
                        w - borderSpacerWidth, borderSpacerHeight,
                        w - borderSpacerWidth, h - borderSpacerHeight,
                        halfW + spacerHalfWidth, h - borderSpacerHeight
                    ),
                    rounding = CornerRounding(radius / 5)
                )

                val lMorph = Morph(start = lTriangle, end = lSquare).toPath(animateProgress)
                val rMorph = Morph(start = rTriangle, end = rSquare).toPath(animateProgress)

                onDrawBehind {
                    drawPath(lMorph, color = theme.controlIconFill)
                    drawPath(rMorph, color = theme.controlIconFill)
                }
            }
            .fillMaxSize()
    )

    return onClick
}