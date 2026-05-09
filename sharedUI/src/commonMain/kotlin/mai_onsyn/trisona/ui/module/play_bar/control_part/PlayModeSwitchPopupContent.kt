package mai_onsyn.trisona.ui.module.play_bar.control_part

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import mai_onsyn.trisona.ui.theme.TrisonaTheme
import mai_onsyn.trisona.ui.util.Config.playerPlayMode
import mai_onsyn.trisona.ui.util.modifier.buttonEffect
import mai_onsyn.trisona.ui.util.modifier.interaction
import mai_onsyn.trisona.ui.util.t
import mai_onsyn.trisona.ui.util.tweenSpecColor
import mai_onsyn.trisona.ui.util.tweenSpecDp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import trisona.sharedui.generated.resources.*

private val itemHeight = 28.dp

@Composable
fun PlayModeSwitchPopupContent(
    modifier: Modifier = Modifier
) {
    val colorScheme = TrisonaTheme.colorScheme
    val radius = TrisonaTheme.shapeScheme.cornerSmall

    val selectedIndex = playerPlayMode
    val highlightY by animateDpAsState(
        targetValue = selectedIndex * itemHeight,
        animationSpec = tweenSpecDp
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .background(TrisonaTheme.colorScheme.surface)
            .padding(TrisonaTheme.PADDING_SMALL)
            .drawWithContent {
                drawRoundRect(
                    color = colorScheme.secondary,
                    cornerRadius = CornerRadius(radius.toPx(), radius.toPx()),
                    size = size.copy(height = itemHeight.toPx()),
                    topLeft = Offset(0f, highlightY.toPx())
                )
                drawContent()
            }
    ) {
        listOf(
            t("ui.play_bar.sequence") to Res.drawable.icon_play_sequence,
            t("ui.play_bar.loop") to Res.drawable.icon_play_loop,
            t("ui.play_bar.repeat") to Res.drawable.icon_play_repeat,
            t("ui.play_bar.shuffle") to Res.drawable.icon_play_shuffle,
            t("ui.play_bar.random") to Res.drawable.icon_play_random
        ).forEachIndexed { index, (text, icon) ->
            val color by animateColorAsState(
                targetValue = if (index == selectedIndex) colorScheme.onPrimary else colorScheme.onSurface,
                animationSpec = tweenSpecColor
            )
            PlayModeSwitchPopupContentItem(
                icon = icon,
                text = text,
                textColor = color,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemHeight)
                    .interaction(
                        onClick = { playerPlayMode = index },
                    )
            )
        }
    }
}

@Composable
private fun PlayModeSwitchPopupContentItem(
    modifier: Modifier = Modifier,
    icon: DrawableResource,
    text: String,
    textColor: Color,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(TrisonaTheme.shapeScheme.cornerSmall))
            .buttonEffect()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.Center)
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(TrisonaTheme.PADDING_MEDIUM))
            Text(
                text = text,
                style = TrisonaTheme.textScheme.labelMedium,
                color = textColor
            )
        }
    }
}