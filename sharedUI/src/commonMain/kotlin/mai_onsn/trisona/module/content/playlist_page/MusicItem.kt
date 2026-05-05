package mai_onsn.trisona.module.content.playlist_page

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mai_onsn.trisona.JImage
import mai_onsn.trisona.module.layout.TipArea
import mai_onsn.trisona.module.play_bar.MorphingPlayPauseButton
import mai_onsn.trisona.theme.LocalAppTheme
import mai_onsn.trisona.theme.MusicItemShape
import mai_onsn.trisona.util.ClickableScaleButtonEffect
import mai_onsn.trisona.util.background
import mai_onsn.trisona.util.formatMillisToTime
import mai_onsn.trisona.util.interaction
import mai_onsyn.trisona.core.TrisonaKotlinInterface.albumSQL
import mai_onsyn.trisona.core.TrisonaKotlinInterface.player
import mai_onsyn.trisona.core.message.Music
import org.jetbrains.compose.resources.painterResource
import trisona.sharedui.generated.resources.Res
import trisona.sharedui.generated.resources.album_unknown

@Composable
fun MusicItem(
    modifier: Modifier = Modifier,
    music: Music,
    ordinal: Int,
    onPlay: () -> Unit = {},
    selected: Boolean = false,
    playing: Boolean = false
) {
    val theme = LocalAppTheme.current

    val album = albumSQL.query(music.albumID)
    Box(
        modifier = modifier
            .clip(MusicItemShape)
//            .background(theme.buttonBaseColor.copy(if (ordinal % 2 == 0) 0.1f else 0.2f))
            .background(
                baseColor = theme.buttonBaseColor.copy(if (ordinal % 2 == 0) 0.1f else 0.2f),
                hoverColor = theme.buttonBaseColor.copy(0.5f),
                pressedColor = theme.buttonBaseColor.copy(0.6f)
            )
            .interaction(
                onDoubleClick = {
                    onPlay()
                }
            ),
    ) {
        Row(
            modifier = Modifier
                .padding(end = 50.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(16.dp))
            Text(
                text = ordinal.toString(),
                fontSize = 12.sp,
                modifier = Modifier.width(32.dp),
                textAlign = TextAlign.Center,
                color = theme.textBaseColor,
            )
            Box(
                modifier = Modifier
                    .width(30.dp)
                    .height(40.dp)
            ) {
                MorphingPlayPauseButton(
                    isPlaying = playing,
                    onToggle = {
                        if (it) {
                            onPlay()
//                            player.specifyMusic(music)
                        }
                    },
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.Center),
                    fill = theme.controlIconFill.copy(0.5f)
                )
            }
            Spacer(Modifier.width(8.dp))
            JImage(
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxHeight()
                    .padding(vertical = 1.dp)
                    .clip(RoundedCornerShape(8.dp)),
                url = album?.picUrl,
                errorPlaceholder = {
                    Image(
                        painter = painterResource(Res.drawable.album_unknown),
                        contentDescription = null,
                    )
                }
            )
            Spacer(Modifier.width(40.dp))
            val titleText = music.title?: "Unknown Title"
            TipArea(
                tip = titleText,
                modifier = Modifier.weight(0.4f),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = titleText,
                    fontSize = 12.sp,
                    color = theme.textBaseColor,
//                        modifier = Modifier.width(200.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.width(16.dp))
            val artistText = music.artists?.joinToString(", ") { it.name } ?: "Unknown Artist"
            TipArea(
                tip = artistText,
                modifier = Modifier.weight(0.2f),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = artistText,
                    fontSize = 12.sp,
                    color = theme.textBaseColor,
//                        modifier = Modifier.width(80.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.width(16.dp))
            val albumText = album?.name ?: "Unknown Album"
            TipArea(
                tip = albumText,
                modifier = Modifier.weight(0.4f),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = albumText,
                    fontSize = 12.sp,
                    color = theme.textBaseColor,
//                        modifier = Modifier.width(120.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.width(16.dp))
        }
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .width(50.dp)
                .align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = formatMillisToTime(music.duration),
                fontSize = 12.sp,
                color = theme.textBaseColor,
            )
        }
    }
}