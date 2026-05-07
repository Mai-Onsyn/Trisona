package mai_onsyn.trisona.ui.module.play_bar.control_part

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import mai_onsyn.trisona.ui.theme.TrisonaTheme
import mai_onsyn.trisona.ui.util.Config.playerVolume
import mai_onsyn.trisona.ui.util.modifier.interaction
import mai_onsyn.trisona.ui.util.tweenSpecFloat

@Composable
fun VolumePopupContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TrisonaTheme.colorScheme.surface)
    ) {
        var progress by remember { mutableStateOf(playerVolume) }
        VerticalSlider(
            progress = progress,
            onProgressChanged = {
                progress = it
                playerVolume = it
            },
            modifier = Modifier
                .width(10.dp)
                .fillMaxHeight()
                .padding(bottom = TrisonaTheme.PADDING_MEDIUM, top = TrisonaTheme.PADDING_MEDIUM + TrisonaTheme.PADDING_LARGE)
                .align(Alignment.Center)
                .pointerHoverIcon(PointerIcon.Hand)
        )
        Text(
            text = (progress * 100).toInt().toString(),
            style = TrisonaTheme.textScheme.labelMedium,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = TrisonaTheme.PADDING_SMALL)
        )
    }
}

@Composable
fun VerticalSlider(
    modifier: Modifier = Modifier,
    progress: Float,
    color: Color = TrisonaTheme.colorScheme.primary,
    trackColor: Color = TrisonaTheme.colorScheme.surfaceLow,
    onProgressChanged: (Float) -> Unit = {}
) {
    var innerProgress by remember { mutableStateOf(progress) }
    val animatedProgress by animateFloatAsState(
        targetValue = innerProgress,
        animationSpec = tweenSpecFloat
    )
    val radius = TrisonaTheme.shapeScheme.corner
    Canvas(
        modifier
            .clip(RoundedCornerShape(radius))
            .interaction(
                onDrag = {
                    val value = (1 - it.changes.first().position.y / size.height).coerceIn(0f, 1f)
                    innerProgress = value
                    onProgressChanged(value)
                },
            )
    ) {
        drawRoundRect(
            color = trackColor,
            size = size,
            cornerRadius = CornerRadius(radius.toPx(), radius.toPx())
        )
        val barHeight = size.height * animatedProgress.coerceIn(0f, 1f)
        drawRoundRect(
            color = color,
            size = size.copy(height = barHeight),
            topLeft = Offset(0f, size.height - barHeight),
            cornerRadius = CornerRadius(radius.toPx(), radius.toPx())
        )
    }
}