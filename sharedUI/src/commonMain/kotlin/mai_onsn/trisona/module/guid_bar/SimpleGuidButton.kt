package mai_onsn.trisona.module.guid_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import mai_onsn.trisona.theme.GuidButtonShape
import mai_onsn.trisona.theme.LocalAppTheme
import mai_onsn.trisona.theme.RightRoundedShape
import mai_onsn.trisona.util.ClickableScaleButtonEffect
import mai_onsn.trisona.util.appShadow
import mai_onsn.trisona.util.interaction

@Composable
fun SimpleGuidButton(
    modifier: Modifier = Modifier,
    label: String,
    selected: Boolean = false,
    onClick: () -> Unit
) {
    val theme = LocalAppTheme.current

    val buttonShape = if (selected) RightRoundedShape else GuidButtonShape
    ClickableScaleButtonEffect(
        hoverScale = 1.04f,
        pressedScale = 0.96f,
        scaleAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = modifier
                .appShadow(shadowShape = buttonShape)
                .fillMaxWidth()
                .height(40.dp)
                .clip(buttonShape)
                .background(if (selected) theme.buttonSelectedBaseColor else theme.buttonBaseColor)
                .interaction(
                    onClick = { onClick() }
                )
                .padding(start = 10.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = label,
                color = if (!selected) theme.buttonSelectedBaseColor else theme.buttonBaseColor,
            )
        }
    }
}