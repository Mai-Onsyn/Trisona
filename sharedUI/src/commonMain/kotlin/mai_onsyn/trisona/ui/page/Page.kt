package mai_onsyn.trisona.ui.page

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import mai_onsyn.trisona.ui.page.home.HomePage
import org.jetbrains.compose.resources.DrawableResource
import trisona.sharedui.generated.resources.Res
import trisona.sharedui.generated.resources.icon_play_sequence
import trisona.sharedui.generated.resources.miss

enum class Page(
    val title: String,
    val icon: DrawableResource,
    val content: @Composable () -> Unit
) {
    HOME("Home", Res.drawable.icon_play_sequence, ::HomePage),
    FAVORITES("Favorites", Res.drawable.icon_play_sequence, {}),
    LOCAL("Local", Res.drawable.icon_play_sequence, {}),
    PLAYLIST("Playlist", Res.drawable.icon_play_sequence, {}),
    SETTINGS("Settings", Res.drawable.icon_play_sequence, {})
}