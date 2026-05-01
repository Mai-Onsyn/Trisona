package mai_onsn.trisona.module.guid_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import mai_onsn.trisona.theme.LocalAppTheme
import mai_onsn.trisona.theme.RightRoundedShape
import mai_onsn.trisona.util.appShadow


enum class GuidPage {
    HOME, FAVORITES, HISTORY, LOCAL, CREATES
}

@Composable
fun GuidBox(
    modifier: Modifier = Modifier,
    onSelectChanged: (guidPage: GuidPage) -> Unit = {}
) {
    val theme = LocalAppTheme.current

    var selectedLabel by remember { mutableStateOf(0) }
    fun selectChange(index: Int): GuidPage = when (index) {
        0 -> GuidPage.HOME
        1 -> GuidPage.FAVORITES
        2 -> GuidPage.HISTORY
        3 -> GuidPage.LOCAL
        4 -> GuidPage.CREATES
        else -> GuidPage.HOME
    }

    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .appShadow(
                    shadowShape = RightRoundedShape
                )
                .clip(RightRoundedShape)
                .fillMaxHeight()
                .width(10.dp)
                .align(Alignment.CenterStart)
                .background(theme.titleBarBase)
        )

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            SimpleGuidButton(
                label = "Home",
                selected = selectedLabel == 0,
                onClick = {
                    selectedLabel = 0
                    onSelectChanged(selectChange(selectedLabel))
                }
            )
            Spacer(modifier = Modifier.height(4.dp))
            SimpleGuidButton(
                label = "Favorite",
                selected = selectedLabel == 1,
                onClick = {
                    selectedLabel = 1
                    onSelectChanged(selectChange(selectedLabel))
                }
            )
            Spacer(modifier = Modifier.height(4.dp))
            SimpleGuidButton(
                label = "History",
                selected = selectedLabel == 2,
                onClick = {
                    selectedLabel = 2
                    onSelectChanged(selectChange(selectedLabel))
                }
            )
            Spacer(modifier = Modifier.height(4.dp))
            SimpleGuidButton(
                label = "Local",
                selected = selectedLabel == 3,
                onClick = {
                    selectedLabel = 3
                    onSelectChanged(selectChange(selectedLabel))
                }
            )
            Spacer(modifier = Modifier.height(4.dp))
            SimpleGuidButton(
                label = "Creates",
                selected = selectedLabel == 4,
                onClick = {
                    selectedLabel = 4
                    onSelectChanged(selectChange(selectedLabel))
                }
            )
        }
    }
}