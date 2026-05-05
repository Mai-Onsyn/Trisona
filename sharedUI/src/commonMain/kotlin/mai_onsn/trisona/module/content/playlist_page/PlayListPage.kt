package mai_onsn.trisona.module.content.playlist_page

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import mai_onsn.trisona.JImage
import mai_onsn.trisona.module.layout.TipArea
import mai_onsn.trisona.module.play_bar.pauseCallback
import mai_onsn.trisona.theme.LocalAppTheme
import mai_onsyn.trisona.core.TrisonaKotlinInterface.albumSQL
import mai_onsyn.trisona.core.TrisonaKotlinInterface.player
import mai_onsyn.trisona.core.play.PlayList

enum class SortOrder {
    DATE, NAME, ARTIST, ALBUM, REGISTERED
}

@Composable
fun BoxScope.PlayListPage(
    modifier: Modifier = Modifier,
    playList: PlayList,
    sort: SortOrder = SortOrder.REGISTERED,
) {
    val theme = LocalAppTheme.current

    val state = rememberLazyListState()
    val list = playList.map { it }
    when (sort) {
        SortOrder.NAME -> list.sortedBy { it.title }
        SortOrder.ARTIST -> list.sortedBy { it.artists?.first()?.name?: "#" }
        SortOrder.ALBUM -> list.sortedBy { it.albumID }
        else -> list.sortedBy { it.title }
    }

    var coverURL: String? = null
    if (playList.info.coverPath.path.url?.isNotEmpty()?: false) {
        coverURL = playList.info.coverPath.path.url
    }
    else for (music in list) {
        albumSQL.query(music.albumID)?.let {
            if (it.picUrl?.isNotEmpty()?: false) {
                coverURL = it.picUrl
                break
            }
        }
    }

    var playerIsPlaying by remember { mutableStateOf(player.isPlaying) }
    var playerPlayingMusic by remember { mutableStateOf(player.currentMusic) }
    LaunchedEffect(Unit) {
        while (true) {
            playerIsPlaying = player.isPlaying
            playerPlayingMusic = player.currentMusic
            delay(100)
        }
    }
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp),
        state = state,
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
//                    .background(Color.Gray)
            ) {
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    JImage(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(16.dp)),
                        url = coverURL,
                    )
                    Spacer(Modifier.width(16.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        TipArea(playList.info.name) {
                            Text(
                                text = playList.info.name,
                                fontSize = 24.sp,
                                color = theme.textBaseColor
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "创建者：${playList.info.creator}",
                            fontSize = 14.sp,
                            color = theme.textPromptColor
                        )
                        Text(
                            text = "创建日期：${playList.info.createDate}",
                            fontSize = 14.sp,
                            color = theme.textPromptColor
                        )
                    }
                }
            }
        }
        items(list) { item ->
            MusicItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                music = item,
                ordinal = list.indexOf(item) + 1,
                onPlay = {
                    player.setPlayList(playList)
                    player.specifyMusic(playList.indexOf(item))
                    if (!player.isPlaying) {
                        pauseCallback?.invoke()
                    }
                },
                playing = playerIsPlaying && playerPlayingMusic == item
            )
        }
    }

    VerticalScrollbar(
        modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
        adapter = rememberScrollbarAdapter(scrollState = state)
    )
}