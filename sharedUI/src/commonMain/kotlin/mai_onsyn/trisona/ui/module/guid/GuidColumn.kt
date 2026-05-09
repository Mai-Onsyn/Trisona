package mai_onsyn.trisona.ui.module.guid

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import mai_onsyn.trisona.ui.currentPage
import mai_onsyn.trisona.ui.page.Page
import mai_onsyn.trisona.ui.theme.TrisonaTheme
import mai_onsyn.trisona.ui.util.modifier.buttonEffect
import mai_onsyn.trisona.ui.util.tweenSpecDp
import mai_onsyn.trisona.util.interaction
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun GuidColumn(
    modifier: Modifier = Modifier,
    buttonSize: Dp = 48.dp
) {
    val colorScheme = TrisonaTheme.colorScheme

    val radius = TrisonaTheme.PADDING_MEDIUM
    var selectedIndex by remember { mutableStateOf(0) }
    val highlightY by animateDpAsState(
        targetValue = selectedIndex * (buttonSize + radius),
        animationSpec = tweenSpecDp
    )
    Column(
        modifier = modifier
            .drawWithContent {
                drawRoundRect(
                    color = colorScheme.secondary,
                    cornerRadius = CornerRadius(radius.toPx(), radius.toPx()),
                    size = size.copy(height = buttonSize.toPx()),
                    topLeft = Offset(0f, highlightY.toPx())
                )
                drawContent()
            }
    ) {
        Page.entries.forEachIndexed { index, page ->
            GuidButton(
                modifier = Modifier
                    .size(buttonSize)
                    .interaction(onClick = {
                        selectedIndex = index
                        currentPage = page
                    }),
                icon = page.icon,
                contentColor = colorScheme.onBackground,
                iconSize = buttonSize * 0.7f
            )
            if (index < Page.entries.lastIndex)
                Spacer(modifier = Modifier.height(TrisonaTheme.PADDING_MEDIUM))
        }
    }
}

@Composable
fun GuidButton(
    modifier: Modifier = Modifier,
    icon: DrawableResource,
    contentColor: Color,
    iconSize: Dp = 18.dp
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(TrisonaTheme.shapeScheme.cornerLarge))
            .buttonEffect(),
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier
                .size(iconSize)
                .align(Alignment.Center)
        )
    }
}