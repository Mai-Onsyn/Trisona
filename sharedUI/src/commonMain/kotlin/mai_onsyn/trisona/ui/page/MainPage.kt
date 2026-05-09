package mai_onsyn.trisona.ui.page

import androidx.compose.foundation.layout.*
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mai_onsyn.trisona.ui.currentPage
import mai_onsyn.trisona.ui.module.guid.GuidColumn
import mai_onsyn.trisona.ui.theme.TrisonaTheme

@Composable
fun MainPage(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Spacer(Modifier.height(64.dp))
        Row(modifier = Modifier.fillMaxHeight()) {
            GuidColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .width((48 + 28 + 15).dp)
                    .padding(start = 28.dp, end = 15.dp)
//                    .background(Color.Red)
            )
            VerticalDivider(color = TrisonaTheme.colorScheme.outline)
            Box(
                modifier = Modifier
                    .padding(horizontal = 15.dp)
            ) {
                currentPage.content()
            }
        }
    }
}