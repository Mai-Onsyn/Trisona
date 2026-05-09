package mai_onsyn.trisona.ui.module.play_bar.control_part

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
import mai_onsyn.trisona.ui.module.layout.AttachedPopup
import mai_onsyn.trisona.ui.theme.TrisonaTheme
import mai_onsyn.trisona.ui.util.CoreState
import mai_onsyn.trisona.ui.util.modifier.buttonEffect
import mai_onsyn.trisona.ui.util.toIntRect
import mai_onsyn.trisona.ui.util.modifier.interaction
import mai_onsyn.trisona.core.TrisonaKotlinInterface.player
import mai_onsyn.trisona.ui.util.Config.animationBaseRate
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import trisona.sharedui.generated.resources.Res
import trisona.sharedui.generated.resources.icon_play_sequence
import trisona.sharedui.generated.resources.icon_play_volume

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
            WithPopup(
                icon = Res.drawable.icon_play_sequence,
                minWidth = 32.dp,
                minHeight = 32.dp,
                maxWidth = 114.dp,
                maxHeight = Dp.Unspecified,
                popupAlignment = Alignment.BottomEnd
            ) { PlayModeSwitchPopupContent(modifier = it) }

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
            WithPopup(
                icon = Res.drawable.icon_play_volume,
                minWidth = 32.dp,
                minHeight = 32.dp,
                maxWidth = 32.dp,
                maxHeight = 128.dp,
                popupAlignment = Alignment.BottomCenter
            ) { VolumePopupContent(modifier = it) }
        }
    }
}

@Composable
private fun WithCircleEffect(
    size: Dp,
    onClick: PointerInputScope.(PointerEvent) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .buttonEffect()
            .interaction(onClick = onClick)
    ) { content() }
}

@Composable
private fun WithPopup(
    icon: DrawableResource,
    minWidth: Dp,
    maxWidth: Dp,
    minHeight: Dp,
    maxHeight: Dp,
    popupAlignment: Alignment,
    content: @Composable (Modifier) -> Unit
) {
    var hovered by remember { mutableStateOf(false) }
    var pressed by remember { mutableStateOf(false) }
    val showProgress by animateFloatAsState(
        targetValue = if (hovered || pressed) 1f else 0f,
        animationSpec = if (hovered || pressed) {
            tween(
                durationMillis = animationBaseRate,
                delayMillis = 200,
                easing = LinearOutSlowInEasing
            )
        } else {
            tween(
                durationMillis = animationBaseRate,
                delayMillis = 300,
                easing = FastOutLinearInEasing
            )
        }
    )
    var layoutRect by remember { mutableStateOf(IntRect.Zero) }
    WithCircleEffect(32.dp, {}, Modifier
        .interaction(
            onHoveredChange = { hovered = it },
            onPressedChange = { pressed = it },
        )
        .onGloballyPositioned { coordinates ->
            layoutRect = coordinates.boundsInWindow().toIntRect()
        }
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = TrisonaTheme.colorScheme.primary,
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.Center)
        )
        AttachedPopup(
            triggerLayoutRect = layoutRect,
            minWidth = minWidth,
            maxWidth = maxWidth,
            minHeight = minHeight,
            maxHeight = maxHeight,
            showProgress = showProgress,
            align = popupAlignment,
            shape = RoundedCornerShape(TrisonaTheme.shapeScheme.corner)
        ) {
            content(Modifier
                .interaction(
                    onHoveredChange = { hovered = it },
                    onPressedChange = { pressed = it },
                )
            )
        }
    }
}