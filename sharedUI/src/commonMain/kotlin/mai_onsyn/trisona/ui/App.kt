package mai_onsyn.trisona.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mai_onsyn.trisona.ui.additional.DynamicTriangleMesh
import mai_onsyn.trisona.ui.additional.GasCanvas
import mai_onsyn.trisona.ui.module.play_bar.PlayBar
import mai_onsyn.trisona.ui.util.Config.isDarkMode
import mai_onsyn.trisona.ui.theme.DarkColorScheme
import mai_onsyn.trisona.ui.theme.LightColorScheme
import mai_onsyn.trisona.ui.theme.TrisonaTheme
import mai_onsyn.trisona.ui.util.Config.playerPlayMode
import mai_onsyn.trisona.ui.util.Config.playerVolume
import mai_onsyn.trisona.core.TrisonaKotlinInterface.player
import mai_onsyn.trisona.core.play.PlayQueue
import mai_onsyn.trisona.ui.page.MainPage
import mai_onsyn.trisona.ui.util.pointerHookListener

@Composable
fun onStart() {
    LaunchedEffect(Unit) {
        player.volume = (playerVolume * 100).toInt()
        player.playMode = PlayQueue.PlayMode.entries[playerPlayMode]
    }
}

@Composable
fun App(vararg args: String) {
    onStart()
    TrisonaTheme(
        colorScheme = if (isDarkMode) DarkColorScheme else LightColorScheme
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(TrisonaTheme.colorScheme.background)
        ) {
//            GasCanvas(modifier = Modifier.fillMaxSize())
//            DynamicTriangleMesh(modifier = Modifier.fillMaxSize())

            MainPage(modifier = Modifier.weight(1f).fillMaxWidth())
            PlayBar(
                modifier = Modifier
                    .height(80.dp)
            )
        }
    }
}