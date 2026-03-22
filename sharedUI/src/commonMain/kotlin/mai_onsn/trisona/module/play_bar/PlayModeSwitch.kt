package mai_onsn.trisona.module.play_bar

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import mai_onsn.trisona.module.util.interaction
import mai_onsn.trisona.theme.LocalAppTheme
import mai_onsyn.trisona.core.TrisonaKotlinInterface.player
import trisona.sharedui.generated.resources.Res
import trisona.sharedui.generated.resources.volume
import trisona.sharedui.generated.resources.volume_slash

@Composable
fun PlayModeSwitch(
    modifier: Modifier = Modifier,
    maxWidth: Dp = 80.dp,
    maxHeight: Dp = 100.dp,
    buttonShape: Shape = CircleShape,
) {
    val theme = LocalAppTheme.current

    var playModeIcon by remember { mutableStateOf(Res.drawable.volume) }
    LaunchedEffect(player.volume) {
        playModeIcon = when (player.volume) {
            0 -> Res.drawable.volume_slash
            else -> Res.drawable.volume
        }
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
    ) {

    }
}