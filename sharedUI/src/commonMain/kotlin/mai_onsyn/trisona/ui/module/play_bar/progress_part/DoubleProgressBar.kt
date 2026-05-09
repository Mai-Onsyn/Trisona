package mai_onsyn.trisona.ui.module.play_bar.progress_part

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import mai_onsyn.trisona.ui.theme.TrisonaTheme
import mai_onsyn.trisona.ui.util.modifier.interaction
import mai_onsyn.trisona.ui.util.tweenSpecFloat

@Composable
fun DoubleProgressBar(
    modifier: Modifier = Modifier,
    primaryProgress: Float,
    secondaryProgress: Float,
    primaryColor: Color = TrisonaTheme.colorScheme.primary,
    secondaryColor: Color = TrisonaTheme.colorScheme.surfaceLowest,
    trackColor: Color = TrisonaTheme.colorScheme.surfaceLow,
    onProgressChanged: (Float) -> Unit = {}
) {
    var isDragging by remember { mutableStateOf(false) }
    var innerPrimaryProgress by remember { mutableStateOf(primaryProgress) }
    LaunchedEffect(primaryProgress) {
        if (!isDragging)
            innerPrimaryProgress = primaryProgress
    }
    val animatedPrimaryProgress by animateFloatAsState(
        targetValue = innerPrimaryProgress,
        animationSpec = tweenSpecFloat
    )

    val radius = TrisonaTheme.shapeScheme.corner
    Canvas(
        modifier = modifier
            .clip(RoundedCornerShape(radius))
            .interaction(
                onPress = { isDragging = true },
                onRelease = {
                    onProgressChanged((it.changes.first().position.x / size.width).coerceIn(0f, 1f))
                    isDragging = false
                },
                onDrag = { innerPrimaryProgress = (it.changes.first().position.x / size.width).coerceIn(0f, 1f) },
            )
    ) {
        val secondaryWidth = size.width * secondaryProgress.coerceIn(0f, 1f)
        val primaryWidth = size.width * animatedPrimaryProgress.coerceIn(0f, 1f)
        drawRoundRect(
            color = trackColor,
            cornerRadius = CornerRadius(radius.toPx(), radius.toPx())
        )
        drawRoundRect(
            color = secondaryColor,
            cornerRadius = CornerRadius(radius.toPx(), radius.toPx()),
            size = Size(secondaryWidth, size.height)
        )
        drawRoundRect(
            color = primaryColor,
            cornerRadius = CornerRadius(radius.toPx(), radius.toPx()),
            size = Size(primaryWidth, size.height)
        )
    }
}