package mai_onsyn.trisona.ui.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import mai_onsyn.trisona.core.TrisonaKotlinInterface.player
import mai_onsyn.trisona.core.message.Music

object CoreState {
    var isPlaying by mutableStateOf(false)
    var currentMusic by mutableStateOf<Music?>(null)

    var playingPosSeconds by mutableStateOf(0f)
    var playingDurationSeconds by mutableStateOf(0f)

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    init {
        player.setOnPlayStateChanged {
            isPlaying = it
        }
        player.setOnMusicChanged { currentMusic = it }

        scope.launch {
            while (isActive) {
                playingPosSeconds = player.playingPosition / 1000
                playingDurationSeconds = player.playingDuration / 1000
                delay(500)
            }
        }
    }

    fun togglePlay() {
        togglePlay(!player.isPlaying)
    }

    fun togglePlay(boolean: Boolean) {
        if (boolean) player.play()
        else player.pause()
    }
}