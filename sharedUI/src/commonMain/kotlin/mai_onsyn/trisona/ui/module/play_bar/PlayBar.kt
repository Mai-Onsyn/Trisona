package mai_onsyn.trisona.ui.module.play_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.dp
import mai_onsyn.trisona.ui.module.play_bar.control_part.StateControl
import mai_onsyn.trisona.ui.module.play_bar.info_part.InfoControl
import mai_onsyn.trisona.ui.module.play_bar.progress_part.ProgressControl
import mai_onsyn.trisona.ui.theme.TrisonaTheme
import mai_onsyn.trisona.ui.util.CoreState.currentMusic

@Composable
fun PlayBar(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .dropShadow(
                shape = RectangleShape,
                shadow = Shadow(
                    radius = 10.dp,
                    spread = 3.dp,
                    color = TrisonaTheme.colorScheme.shadow,
                    blendMode = BlendMode.Overlay
                )
            )
            .fillMaxWidth()
            .background(TrisonaTheme.colorScheme.surface)
    ) {
        // 进度条和控制部分
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .height(60.dp)
                .padding(horizontal = 256.dp)
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            // 控制部分
            StateControl(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.5f)
            )
            // 进度条
            ProgressControl(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }

        // 信息部分
        InfoControl(
            music = currentMusic,
            modifier = Modifier
                .height(60.dp)
                .width(256.dp)
                .align(Alignment.CenterStart)
                .padding(start = TrisonaTheme.PADDING_MEDIUM)
        )
    }
}