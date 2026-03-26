package mai_onsn.trisona.module.play_bar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.delay
import mai_onsn.trisona.Config.playMode
import mai_onsn.trisona.module.util.ClickableScaleButtonEffect
import mai_onsn.trisona.module.util.interaction
import mai_onsn.trisona.module.util.tweenSpecFloat
import mai_onsn.trisona.theme.LocalAppTheme
import mai_onsn.trisona.theme.parallelogramPath
import mai_onsyn.trisona.core.TrisonaKotlinInterface.player
import mai_onsyn.trisona.core.play.PlayQueue
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import trisona.sharedui.generated.resources.Res
import trisona.sharedui.generated.resources.icon_play_loop
import trisona.sharedui.generated.resources.icon_play_random
import trisona.sharedui.generated.resources.icon_play_repeat
import trisona.sharedui.generated.resources.icon_play_sequence
import trisona.sharedui.generated.resources.icon_play_shuffle
import kotlin.math.PI

val modeItemShape = GenericShape { size, _ -> parallelogramPath(size, (PI/2.25).toFloat(), 4.dp.value) }

@Composable
fun PlayModeSwitch(
    modifier: Modifier = Modifier,
    maxWidth: Dp = 100.dp,
    maxHeight: Dp = 155.dp,
    buttonShape: Shape = CircleShape,
) {
    val theme = LocalAppTheme.current

    var playModeIcon by remember { mutableStateOf(Res.drawable.icon_play_sequence) }
    val playModeIconCollection = remember {
        arrayOf(
            Res.drawable.icon_play_sequence,
            Res.drawable.icon_play_loop,
            Res.drawable.icon_play_repeat,
            Res.drawable.icon_play_shuffle,
            Res.drawable.icon_play_random
        )
    }
    LaunchedEffect(playMode) {
        playModeIcon = playModeIconCollection[playMode]
        delay(500)
    }

    var isHovered by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }
    var showPopup by remember { mutableStateOf(false) }

    BoxWithConstraints(
        modifier = modifier
            .clip(buttonShape)
            .interaction(
                onPressedChange = { isPressed = it },
                onHoveredChange = { isHovered = it },
                onHoverEnter = { showPopup = true },
            )
//            .background(Color.Red)
    ) {

        val popupShowProgress by animateFloatAsState(
            targetValue = if (isHovered || isPressed) 1f else 0f,
            animationSpec = tweenSpecFloat,
            finishedListener = {
                if (it == 0f) showPopup = false
            }
        )

        Icon(
            painter = painterResource(playModeIcon),
            contentDescription = null,
            tint = theme.controlIconFill,
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.Center)
        )

        if (showPopup) {
            Popup(
                alignment = Alignment.BottomEnd,
                onDismissRequest = { showPopup = false },
            ) {
                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            alpha = popupShowProgress
                            shadowElevation = 12.dp.toPx()
                            shape = RoundedCornerShape(maxWidth / 2)
                            clip = false
                            ambientShadowColor = theme.backGroundShadow.copy(0.3f)
                            spotShadowColor = theme.backGroundShadow.copy(0.5f)
                        }
                        .width(genericButtonSize + (maxWidth - genericButtonSize) * popupShowProgress)
                        .height(genericButtonSize + (maxHeight - genericButtonSize) * popupShowProgress)
                        .clip(RoundedCornerShape(8.dp))
                        .background(theme.popupBaseColor)
                        .interaction(
                            onPressedChange = { isPressed = it },
                            onHoveredChange = { isHovered = it },
                        )
                ) {
                    Column(
                        verticalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxSize()
//                            .padding(4.dp)
                    ) {
                        val currentModeResource = playModeIconCollection[playMode]
                        val modeNames = remember {
                            arrayOf("顺序播放", "列表循环", "单曲循环", "随机播放", "单曲随机")
                        }

                        for (i in 0..4) {
                            SwitchButton(
                                icon = playModeIconCollection[i],
                                description = modeNames[i],
                                id = i,
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .fillMaxWidth()
                                    .height((genericButtonSize.value * 0.75).dp)
                            )
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun SwitchButton(
    modifier: Modifier = Modifier,
    icon: DrawableResource,
    description: String,
    id: Int
) {
    val theme = LocalAppTheme.current

    ClickableScaleButtonEffect(
        hoverScale = 1.04f,
        pressedScale = 0.96f,
        onClick = {
            println("ChangePlayMode: $id")
            playMode = id
            player.setPlayMode(id)
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .shadow(
                    elevation = 4.dp,
                    ambientColor = theme.backGroundShadow.copy(alpha = 0.3f),
                    spotColor = theme.backGroundShadow.copy(alpha = 0.5f),
                    shape = if (id != playMode) modeItemShape else RoundedCornerShape(4.dp)
                )
                .then(
                    if (id != playMode) Modifier.clip(modeItemShape).background(theme.buttonBaseColor)
                    else Modifier.clip(RoundedCornerShape(4.dp)).background(theme.buttonSelectedBaseColor)
                )
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = theme.textLightColor,
                modifier = Modifier
                    .size((genericButtonSize.value * 0.7).dp)
                    .padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = description,
                fontSize = 12.sp,
                color = theme.textLightColor,
            )
        }
    }
}