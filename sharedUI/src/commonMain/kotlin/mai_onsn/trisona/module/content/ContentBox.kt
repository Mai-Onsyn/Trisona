package mai_onsn.trisona.module.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import mai_onsn.trisona.module.content.playlist_page.PlayListPage
import mai_onsn.trisona.module.guid_bar.GuidPage
import mai_onsyn.trisona.core.TrisonaKotlinInterface.testPlayList
import mai_onsyn.trisona.core.message.PlayListInfo
import mai_onsyn.trisona.core.play.PlayList

@Composable
fun ContentBox(
    modifier: Modifier = Modifier,
    page: GuidPage = GuidPage.HOME
) {
    Box(
        modifier = modifier
//            .background(Color.White)
    ) {
        when (page) {
            GuidPage.HOME -> {

            }
            GuidPage.CREATES -> {
                PlayListPage(
                    modifier = Modifier
                        .fillMaxSize(),
                    playList = testPlayList!!
                )
            }
            else -> {}
        }
    }
}