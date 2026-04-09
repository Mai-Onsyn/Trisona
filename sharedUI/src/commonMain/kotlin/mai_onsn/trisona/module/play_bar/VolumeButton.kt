package mai_onsn.trisona.module.play_bar

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.delay
import mai_onsn.trisona.Config.animationBaseRate
import mai_onsn.trisona.Config.volume
import mai_onsn.trisona.module.layout.PopupBox
import mai_onsn.trisona.module.util.interaction
import mai_onsn.trisona.module.util.tweenSpecFloat
import mai_onsn.trisona.theme.LocalAppTheme
import mai_onsyn.trisona.core.TrisonaKotlinInterface.player
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.skiko.Cursor
import trisona.sharedui.generated.resources.Res
import trisona.sharedui.generated.resources.icon_play_volume
import trisona.sharedui.generated.resources.icon_play_volume_slash

@Composable
fun VolumeButton(
    modifier: Modifier = Modifier,
    width: Dp,
    maxHeight: Dp = 120.dp,
    buttonShape: Shape = CircleShape,
) {
    val theme = LocalAppTheme.current

    var volumeIcon by remember { mutableStateOf(Res.drawable.icon_play_volume) }
    LaunchedEffect(player.volume) {
        volumeIcon = when (player.volume) {
            0 -> Res.drawable.icon_play_volume_slash
            else -> Res.drawable.icon_play_volume
        }
        delay(500)
    }

    PopupBox(
        modifier = modifier
            .clip(buttonShape),
        popupShape = RoundedCornerShape(width / 2),
        maxWidth = width,
        maxHeight = maxHeight,
        minWidth = width,
        minHeight = width,
        popupAlignment = Alignment.BottomCenter,
        interactionContentAlignment = Alignment.Center,
        interactionContent = {
            Icon(
                painter = painterResource(volumeIcon),
                contentDescription = null,
                tint = theme.controlIconFill,
                modifier = Modifier
                    .size(20.dp)
//                    .align(Alignment.Center)
            )
        }
    ) {
        VolumeSlider(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .width(10.dp)
                .pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)))
        )
    }
}

@Composable
fun VolumeSlider(
    modifier: Modifier = Modifier,
) {
    val theme = LocalAppTheme.current

    var progress by remember { mutableStateOf(player.volume / 100f) }
    val animateProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = animationBaseRate, easing = LinearOutSlowInEasing),
    )

    BoxWithConstraints(
        modifier = modifier
            .clip(RoundedCornerShape(5.dp))
            .background(theme.backgroundColor)
            .interaction(
                onDrag = { event ->
                    val change = event.changes.first()
                    progress = 1 - (change.position.y / size.height).coerceIn(0f, 1f)
                    player.volume = (progress * 100).toInt()
                    volume = player.volume
                },
                onPress = { event ->
                    progress = 1 - (event.changes.first().position.y / size.height).coerceIn(0f, 1f)
                    player.volume = (progress * 100).toInt()
                    volume = player.volume
                }
            )
    ) {
        Canvas(Modifier.fillMaxSize()) {
            drawRoundRect(
                color = theme.progressBarColor,
                size = size.copy(height = size.height * animateProgress),
                topLeft = Offset(0f, size.height * (1 - animateProgress)),
                cornerRadius = CornerRadius(size.width)
            )
        }
    }
}