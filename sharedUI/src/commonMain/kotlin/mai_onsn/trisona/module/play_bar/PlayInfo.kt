package mai_onsn.trisona.module.play_bar

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
import mai_onsn.trisona.JImage
import mai_onsn.trisona.module.util.TipArea
import mai_onsn.trisona.theme.LocalAppTheme
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
            val tiText = music.title?: "Unknown Title"
            val arText = music.artists?.joinToString(", ") { it.name } ?: "Unknown Artist"
            TipArea(tiText) {
                Text(
                    text = tiText,
                    fontSize = 14.sp,
                    color = theme.textBaseColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            TipArea(arText) {
                Text(
                    text = arText,
                    fontSize = 12.sp,
                    color = theme.textPromptColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}