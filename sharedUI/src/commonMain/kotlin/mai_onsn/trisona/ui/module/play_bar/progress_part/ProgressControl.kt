package mai_onsn.trisona.ui.module.play_bar.progress_part

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import mai_onsn.trisona.ui.theme.TrisonaTheme
import mai_onsn.trisona.ui.util.CoreState.playingDurationSeconds
import mai_onsn.trisona.ui.util.CoreState.playingPosSeconds
import mai_onsn.trisona.ui.util.formatMillisToTime
import mai_onsn.trisona.ui.util.tweenSpecFloat
import mai_onsn.trisona.util.interaction
import mai_onsyn.trisona.core.TrisonaKotlinInterface.player

@Composable
fun ProgressControl(
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = formatMillisToTime(playingPosSeconds * 1000),
            style = TrisonaTheme.textScheme.labelSmall
        )
        Spacer(modifier = Modifier.width(TrisonaTheme.PADDING_MEDIUM))

        var progressRegionHovered by remember { mutableStateOf(false) }
        var progressRegionPressed by remember { mutableStateOf(false) }
        val scaleProgress by animateFloatAsState(
            targetValue = if (progressRegionHovered || progressRegionPressed) 0f else 1f,
            animationSpec = tweenSpecFloat
        )
        Box(
            modifier = Modifier
                .pointerHoverIcon(PointerIcon.Hand)
                .height(16.dp)
                .fillMaxWidth()
                .weight(1f)
                .interaction(
                    onHoveredChange = { progressRegionHovered = it },
                    onPressedChange = { progressRegionPressed = it }
                )
        ) {
            DoubleProgressBar(
                primaryProgress = (playingPosSeconds / playingDurationSeconds).let { if (it.isNaN()) 0f else it },
                secondaryProgress = 1f,
                onProgressChanged = {
                    player.seek((it * playingDurationSeconds * 1000).toInt())
                },
                modifier = Modifier
                    .padding((4 * scaleProgress).dp)
                    .fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.width(TrisonaTheme.PADDING_MEDIUM))
        Text(
            text = formatMillisToTime(playingDurationSeconds * 1000),
            style = TrisonaTheme.textScheme.labelSmall
        )
    }
}