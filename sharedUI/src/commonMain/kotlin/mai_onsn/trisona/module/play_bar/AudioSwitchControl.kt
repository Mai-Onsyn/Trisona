package mai_onsn.trisona.module.play_bar

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import mai_onsn.trisona.module.util.toComposePath
import mai_onsn.trisona.theme.LocalAppTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AudioSwitchControl(
    modifier: Modifier,
    flip: Boolean
) {
    val theme = LocalAppTheme.current
    Box(
        modifier = modifier
            .drawWithCache {

                val w = size.width
                val h = size.height

                val radius = size.minDimension / 2
                val lineWidth = 5f
                val lineVerticalPadding = 5f
                val offset = 0.25f * radius + lineWidth / 4f

                val triangle = RoundedPolygon(
                    numVertices = 3,
                    radius = radius,
                    centerX = w / 2 - offset,
                    centerY = h / 2,
                    rounding = CornerRounding(radius / 10)
                )

                val line = RoundedPolygon(
                    vertices = floatArrayOf(
                        w / 2 + radius - lineWidth / 2 - offset, lineVerticalPadding,
                        w / 2 + radius + lineWidth / 2 - offset, lineVerticalPadding,
                        w / 2 + radius + lineWidth / 2 - offset, h - lineVerticalPadding,
                        w / 2 + radius - lineWidth / 2 - offset, h - lineVerticalPadding
                    ),
                    rounding = CornerRounding(radius / 10)
                )

                onDrawBehind {
                    withTransform({
                        scale(
                            scaleX = if (flip) -1f else 1f,
                            scaleY = 1f,
                            pivot = center
                        )
                    }) {
                        drawPath(triangle.toComposePath(), color = theme.controlIconFill)
                        drawPath(line.toComposePath(), color = theme.controlIconFill)
                    }
                }
            }
    )
}