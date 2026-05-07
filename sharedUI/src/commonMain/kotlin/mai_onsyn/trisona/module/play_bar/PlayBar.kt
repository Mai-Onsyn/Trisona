package mai_onsyn.trisona.module.play_bar

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import mai_onsyn.trisona.util.ClickableRoundIconButtonScaleEffect
import mai_onsyn.trisona.util.formatMillisToTime
import mai_onsyn.trisona.util.interaction
import mai_onsyn.trisona.theme.parallelogramPath
import mai_onsyn.trisona.theme.LocalAppTheme
import mai_onsyn.trisona.core.TrisonaKotlinInterface.artistSQL
import mai_onsyn.trisona.core.TrisonaKotlinInterface.audioSQL
import mai_onsyn.trisona.core.TrisonaKotlinInterface.musicSQL
import mai_onsyn.trisona.core.TrisonaKotlinInterface.player
import mai_onsyn.trisona.core.log
import mai_onsyn.trisona.core.message.Music
import org.jetbrains.skiko.Cursor
import kotlin.math.PI

internal val progressHeight = 10.dp
internal var adjustingProgress = false
internal val genericButtonSize = 35.dp

var pauseCallback: (() -> Unit)? = null

val playBarShape = GenericShape { size, _ -> parallelogramPath(size, (PI/2.25).toFloat(), 8f) }

@Composable
fun PlayBar(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                clip = true
                shape = playBarShape
            }
            .background(LocalAppTheme.current.themeMain.copy(0.5f))
            .clip(playBarShape)
    ) {
        //控制按钮
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 28.dp)
                .width(180.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            PlayModeSwitch(
                size = genericButtonSize,
            )

            //后退
            ClickableRoundIconButtonScaleEffect (
                modifier = Modifier.size(genericButtonSize),
                onClick = { player.previous() }
            ) {
                AudioSwitchControl(
                    modifier = Modifier.size(24.dp),
                    flip = true
                )
            }

            //播放/暂停
//            var clickAction: ((Boolean) -> Unit)? = null
            var paused by remember { mutableStateOf(true) }
            LaunchedEffect(Unit) {
                player.setOnQueueEnd {
                    paused = true
                    player.pause()
                }

                pauseCallback = {
                    paused = !paused
                    if (paused) player.pause()
                    else player.play()
                }
            }
            ClickableRoundIconButtonScaleEffect (
                modifier = Modifier.size(40.dp),
                onClick = { pauseCallback?.invoke() }
            ) {
                MorphingPlayPauseButton(
                    isPlaying = !paused,
                    onToggle = {  },
                    modifier = Modifier
                        .size(28.dp)
                )
            }

            //前进
            ClickableRoundIconButtonScaleEffect (
                modifier = Modifier.size(genericButtonSize),
                onClick = { player.next() }
            ) {
                AudioSwitchControl(
                    modifier = Modifier
                        .size(24.dp),
                    flip = false
                )
            }

            VolumeButton(
                modifier = Modifier
                    .size(genericButtonSize),
                width = genericButtonSize
            )
        }

        //进度条
        val interactionSource = remember { MutableInteractionSource() }
        val isHovered by interactionSource.collectIsHoveredAsState()
        var isInteracting by remember { mutableStateOf(false) }

        val animateScaleRadius by animateDpAsState(if (isHovered || isInteracting) 8.dp else 0.dp)
        var musicProgress by remember { mutableStateOf(0f) }
        var passedTime by remember { mutableStateOf("00:00") }
        var totalTime by remember { mutableStateOf("00:00") }
        LaunchedEffect(player) {
            while (true) {
                if (!adjustingProgress) {
                    musicProgress = player.playingPosition / player.playingDuration
                    passedTime = formatMillisToTime(player.playingPosition)
                    totalTime = formatMillisToTime(player.playingDuration)
                }

                delay(500)
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
                .padding(horizontal = 200.dp - animateScaleRadius)
                .height(progressHeight + 8.dp)
        ) {
            Text(
                text = passedTime,
                modifier = Modifier
                    .width(60.dp)
                    .fillMaxHeight(),
                textAlign = TextAlign.End,
                fontSize = 12.sp,
                color = LocalAppTheme.current.textPromptColor
            )
            Spacer(Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .hoverable(interactionSource = interactionSource)
                    .pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)))
                    .interaction(
                        onPressedChange = { isInteracting = it },
                        onDrag = { event ->
                            //TODO: 时间戳和歌词显示
                        }
                    )
                    .weight(1f)
            ) {
                DoubleProgressBar(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .height(progressHeight + animateScaleRadius)
                        .clip(RoundedCornerShape((progressHeight + animateScaleRadius) / 2)),
                    mainProgress = musicProgress,
                    bufferProgress = 1f
                )
            }
            Spacer(Modifier.width(10.dp))
            Text(
                text = totalTime,
                modifier = Modifier
                    .width(60.dp)
                    .fillMaxHeight(),
                textAlign = TextAlign.Start,
                fontSize = 12.sp,
                color = LocalAppTheme.current.textPromptColor
            )
        }

        //播放信息/封面
        val emptyMusic = Music()
        val currentMusic by produceState(initialValue = emptyMusic, key1 = player.currentMusic) {
            val musicID = player.currentMusic?.id ?: emptyMusic.id
            log.debug("switched to id: {}, music: {}", musicID, player.currentMusic)
            val result = musicSQL.query(musicID, audioSQL, artistSQL) ?: emptyMusic
            value = result
        }
        PlayInfo(
            modifier = Modifier
                .fillMaxHeight()
                .width(220.dp)
                .padding(vertical = 10.dp)
                .padding(start = 20.dp)
                .align(Alignment.CenterStart),
            music = currentMusic
        )
    }
}