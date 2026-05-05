package mai_onsn.trisona.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mai_onsn.trisona.ui.additional.DynamicTriangleMesh
import mai_onsn.trisona.ui.additional.GasCanvas
import mai_onsn.trisona.ui.module.play_bar.PlayBar
import mai_onsn.trisona.ui.util.Config.isDarkMode
import mai_onsn.trisona.ui.theme.DarkColorScheme
import mai_onsn.trisona.ui.theme.LightColorScheme
import mai_onsn.trisona.ui.theme.TrisonaTheme

@Composable
fun App(vararg args: String) {
    TrisonaTheme(
        colorScheme = if (isDarkMode) DarkColorScheme else LightColorScheme
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(TrisonaTheme.colorScheme.background)
        ) {
//            GasCanvas(modifier = Modifier.fillMaxSize())
//            DynamicTriangleMesh(modifier = Modifier.fillMaxSize())

            PlayBar(
                modifier = Modifier
                    .height(80.dp)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}