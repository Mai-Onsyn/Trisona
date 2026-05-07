package mai_onsyn.trisona.module.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import mai_onsyn.trisona.theme.AppColors
import mai_onsyn.trisona.theme.LocalAppTheme

@Composable
fun TitleBackBar(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val theme = LocalAppTheme.current
    Box(
        modifier = modifier
            .shadow(
                elevation = 12.dp,
                shape = BottomRoundedShape,
                ambientColor = theme.backGroundShadow.copy(alpha = 0.3f),
                spotColor = theme.backGroundShadow.copy(alpha = 0.5f),
            )
            .background(
                color = theme.titleBarBase,
                shape = BottomRoundedShape
            )
            .zIndex(1f)
            .drawWithContent {
                drawRect(
                    brush = Brush.horizontalGradient(
                        0.0f to theme.titleBarGradient,
                        0.5f to Color.Transparent,
                    ),
//                    blendMode = BlendMode.Overlay
                )
                drawContent()
            }
    ) {
//        DynamicTriangleMesh(
//            maxAlpha = 0.5f,
//            triangleSize = 60f
//        )
//        Image(
//            painter = painterResource(Res.drawable.hexa_back_light),
//            contentDescription = null,
//            contentScale = ContentScale.None,
//            modifier = Modifier
//                .wrapContentSize(unbounded = true, align = Alignment.TopStart)
//                .graphicsLayer {
//                    scaleX = 0.6f
//                    scaleY = 0.6f
//                    transformOrigin = TransformOrigin(0f, 0f)
//                }
//                .graphicsLayer {
//                    scaleX = -1f
//                    scaleY = 1f
//                    alpha = 0.99f
//                }
//                .offset(x = (100).dp, y = (-10).dp)
//                .drawWithContent {
//                    drawContent()
//                    drawRect(
//                        brush = Brush.horizontalGradient(
//                            0.7f to Color.Transparent,
//                            1.0f to Color.Black
//                        ),
//                        blendMode = BlendMode.DstIn
//                    )
//                }
//        )

        content()
    }
}

val BottomRoundedShape = RoundedCornerShape(
    topStart = 0.dp,   // 左上角直角
    topEnd = 0.dp,     // 右上角直角
    bottomStart = 8.dp, // 左下角圆角半径
    bottomEnd = 8.dp    // 右下角圆角半径
)