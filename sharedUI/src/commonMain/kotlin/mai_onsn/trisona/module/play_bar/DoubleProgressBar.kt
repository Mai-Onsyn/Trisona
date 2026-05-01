package mai_onsn.trisona.module.play_bar

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults.drawStopIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import mai_onsn.trisona.Config.animationBaseRate
import mai_onsn.trisona.util.interaction
import mai_onsn.trisona.theme.LocalAppTheme
import mai_onsyn.trisona.core.TrisonaKotlinInterface.player

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoubleProgressBar(
    modifier: Modifier = Modifier,
    mainProgress: Float,
    bufferProgress: Float
) {
    val theme = LocalAppTheme.current
    Box(
        modifier = modifier
            .background(theme.backgroundColor)
    ) {
        LinearProgressIndicator(
            progress = { bufferProgress },
            modifier = Modifier
                .fillMaxSize(),
            color = Color.Gray.copy(0.2f),
            trackColor = Color.Transparent,
            strokeCap = StrokeCap.Round,
            drawStopIndicator = {
                drawStopIndicator(
                    drawScope = this,
                    stopSize = 0.dp,
                    color = Color.Transparent,
                    strokeCap = StrokeCap.Round,
                )
            }
        )

        var progress by remember { mutableStateOf(mainProgress) }
        val animateProgress by animateFloatAsState(
            targetValue = progress,
            animationSpec = tween(durationMillis = animationBaseRate, easing = LinearOutSlowInEasing),
//            finishedListener = { adjustingProgress = false }
        )
        LaunchedEffect(mainProgress) {
            if (!adjustingProgress) {
                progress = mainProgress
            }
        }
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .interaction(
                    onDrag = { event ->
                        val change = event.changes.first()
                        progress = (change.position.x / size.width).coerceIn(0f, 1f)
                    },
                    onPress = { event ->
                        progress = (event.changes.first().position.x / size.width).coerceIn(0f, 1f)
                        adjustingProgress = true
                    },
                    onRelease = {
                        Thread.ofVirtual().name("SeekThread").start {
                            try {
                                player.seek((player.playingDuration * progress).toInt())
                            } finally {
                                adjustingProgress = false
                            }
                        }
                    }
                )
        ) {
            Canvas(Modifier.fillMaxSize()) {
                drawRoundRect(
                    color = theme.progressBarColor,
                    size = size.copy(width = size.width * animateProgress),
                    cornerRadius = CornerRadius(size.height)
                )
            }
        }
    }
}