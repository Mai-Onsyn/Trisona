package mai_onsyn.trisona.module.play_bar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mai_onsyn.trisona.JImage
import mai_onsyn.trisona.module.layout.TipArea
import mai_onsyn.trisona.theme.LocalAppTheme
import mai_onsyn.trisona.core.TrisonaKotlinInterface.albumSQL
import mai_onsyn.trisona.core.message.Music
import org.jetbrains.compose.resources.painterResource
import trisona.sharedui.generated.resources.Res
import trisona.sharedui.generated.resources.album_unknown

@Composable
fun PlayInfo(
    modifier: Modifier = Modifier,
    music: Music,
) {
    val theme = LocalAppTheme.current
    Row(
        modifier = modifier
//            .background(Color.Red)
    ) {
        JImage(
            url = albumSQL.query(music.albumID)?.picUrl,
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop,
            loadingPlaceholder = {
                Box(modifier.background(Color.LightGray))
            },
            errorPlaceholder = {
                Image(
                    painter = painterResource(Res.drawable.album_unknown),
                    contentDescription = null,
                )
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Center
        ) {
            val titleText = music.title?: "Unknown Title"
            val artistText = music.artists?.joinToString(", ") { it.name } ?: "Unknown Artist"
            TipArea(titleText) {
                Text(
                    text = titleText,
                    fontSize = 14.sp,
                    color = theme.textBaseColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            TipArea(artistText) {
                Text(
                    text = artistText,
                    fontSize = 12.sp,
                    color = theme.textPromptColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}