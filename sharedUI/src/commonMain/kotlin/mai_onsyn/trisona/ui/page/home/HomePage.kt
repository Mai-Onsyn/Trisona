package mai_onsyn.trisona.ui.page.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import mai_onsyn.trisona.ui.theme.TrisonaTheme

@Composable
fun HomePage(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = TrisonaTheme.shapeScheme.cornerLarge, topEnd = TrisonaTheme.shapeScheme.cornerLarge))
            .background(TrisonaTheme.colorScheme.surface)
    )
}