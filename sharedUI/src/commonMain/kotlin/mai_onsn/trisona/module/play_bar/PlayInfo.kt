package mai_onsn.trisona.module.play_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import mai_onsn.trisona.JImage
import mai_onsyn.trisona.core.TrisonaKotlinInterface
import mai_onsyn.trisona.core.message.MusicMessage

@Composable
fun PlayInfo(
    modifier: Modifier = Modifier,
    musicMessage: MusicMessage,
) {
    Box(
        modifier = modifier
            .background(Color.Red)
    ) {
        JImage(
            url = TrisonaKotlinInterface.albumSQL.query(musicMessage.albumID)?.picUrl,
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop,
            loadingPlaceholder = {
                Box(modifier.background(Color.LightGray))
            },
            errorPlaceholder = {
                Box(modifier.background(Color.Blue))
            }
        )
    }
}