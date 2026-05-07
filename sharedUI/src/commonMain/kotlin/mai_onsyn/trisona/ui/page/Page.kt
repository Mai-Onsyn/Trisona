package mai_onsyn.trisona.ui.page

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.DrawableResource

enum class Page(
    val title: String,
    val icon: DrawableResource?,
    val content: @Composable () -> Unit
) {
    HOME("Home", null, {}),
    FAVORITES("Favorites", null, {}),
    LOCAL("Local", null, {}),
    PLAYLIST("Playlist", null, {}),
    SETTINGS("Settings", null, {})
}