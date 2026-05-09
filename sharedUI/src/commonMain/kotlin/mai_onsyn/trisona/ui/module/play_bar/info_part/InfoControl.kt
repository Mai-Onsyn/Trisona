package mai_onsyn.trisona.ui.module.play_bar.info_part

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import mai_onsyn.trisona.core.TrisonaKotlinInterface.albumSQL
import mai_onsyn.trisona.core.message.Music
import mai_onsyn.trisona.ui.module.JImage
import mai_onsyn.trisona.ui.module.TipArea
import mai_onsyn.trisona.ui.theme.TrisonaTheme
import trisona.sharedui.generated.resources.Res
import trisona.sharedui.generated.resources.album_unknown

@Composable
fun InfoControl(
    modifier: Modifier = Modifier,
    music: Music? = null
) {
    Row(
        modifier = modifier//.background(Color.Yellow)
    ) {
        JImage(
            if (music == null) null else albumSQL.query(music.albumID)?.picUrl,
            errorPlaceholder = {
                JImage(
                    resource = Res.drawable.album_unknown,
                    modifier = Modifier.fillMaxSize()
                )
            },
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(TrisonaTheme.shapeScheme.corner))
        )
        Spacer(Modifier.width(TrisonaTheme.PADDING_MEDIUM))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(vertical = TrisonaTheme.PADDING_MINI)
        ) {
            Spacer(Modifier.weight(1f))
            val musicName = music?.title ?: "未知歌曲"
            TipArea(musicName) {
                Text(
                    text = musicName,
                    style = TrisonaTheme.textScheme.bodyMedium,
                    modifier = it,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
//            Spacer(Modifier.height(TrisonaTheme.PADDING_SMALL))
            val artistName = music?.artists?.mapNotNull { it.name }?.joinToString("、") ?: "未知歌手"
            TipArea(artistName) {
                Text(
                    text = artistName,
                    style = TrisonaTheme.textScheme.labelMedium,
                    modifier = it,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.weight(1f))
        }
    }
}