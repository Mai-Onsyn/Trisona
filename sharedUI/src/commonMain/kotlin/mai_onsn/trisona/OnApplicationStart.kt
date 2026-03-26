package mai_onsn.trisona

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import mai_onsn.trisona.Config.playMode
import mai_onsn.trisona.Config.volume
import mai_onsyn.trisona.core.TrisonaKotlinInterface.player

@Composable
fun onApplicationStart() {
    LaunchedEffect(Unit) {
        player.setVolume(volume)
        player.setPlayMode(playMode)
    }
}