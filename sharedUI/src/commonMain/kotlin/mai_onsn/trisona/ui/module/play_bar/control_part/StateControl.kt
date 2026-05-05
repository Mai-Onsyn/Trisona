package mai_onsn.trisona.ui.module.play_bar.control_part

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import mai_onsn.trisona.ui.theme.TrisonaTheme
import mai_onsn.trisona.ui.util.CoreState
import mai_onsn.trisona.ui.util.modifier.buttonEffect
import mai_onsn.trisona.util.interaction
import mai_onsyn.trisona.core.TrisonaKotlinInterface.player

@Composable
fun StateControl(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(TrisonaTheme.PADDING_MEDIUM),
            modifier = Modifier.align(Alignment.Center)
        ) {
            // 播放模式
            WithCircleEffect(32.dp, {  }) {

            }

            // 上一曲按钮
            WithCircleEffect(32.dp, { player.previous() }) {
                PollingShape(
                    fill = TrisonaTheme.colorScheme.primary,
                    flip = true,
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.Center),
                )
            }

            // 播放按钮
            WithCircleEffect(36.dp, { CoreState.togglePlay() }) {
                MorphingPlayPauseButton(
                    isPlaying = CoreState.isPlaying,
                    onToggle = {},
                    fill = TrisonaTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(28.dp)
                        .align(Alignment.Center)
                )
            }

            // 下一曲按钮
            WithCircleEffect(32.dp, { player.next() }) {
                PollingShape(
                    fill = TrisonaTheme.colorScheme.primary,
                    flip = false,
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.Center),
                )
            }

            // 音量
            WithCircleEffect(32.dp, {  }) {

            }
        }
    }
}

@Composable
private fun WithCircleEffect(
    size: Dp,
    onClick: PointerInputScope.(PointerEvent) -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .buttonEffect()
            .interaction(onClick = onClick)
    ) {
        content()
    }
}