package mai_onsyn.trisona.ui.module.play_bar.control_part

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import mai_onsyn.trisona.ui.util.toComposePath

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PollingShape(
    modifier: Modifier,
    flip: Boolean,
    fill: Color = Color.White
) {
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
                    rounding = CornerRounding(radius / 8)
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
                        drawPath(triangle.toComposePath(), color = fill)
                        drawPath(line.toComposePath(), color = fill)
                    }
                }
            }
    )
}