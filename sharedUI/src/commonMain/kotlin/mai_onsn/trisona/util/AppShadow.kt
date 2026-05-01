package mai_onsn.trisona.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import mai_onsn.trisona.theme.LocalAppTheme

@Composable
fun Modifier.appShadow(
    shadowShape: Shape,
    opacity: Float = 1.0f,
    doClip: Boolean = false
): Modifier {
    val theme = LocalAppTheme.current
    return Modifier
        .graphicsLayer {
            alpha = opacity
            shadowElevation = 12.dp.toPx()
            shape = shadowShape
            clip = doClip
            ambientShadowColor = theme.backGroundShadow.copy(0.3f)
            spotShadowColor = theme.backGroundShadow.copy(0.5f)
        }
}