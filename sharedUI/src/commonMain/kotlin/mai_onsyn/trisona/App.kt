package mai_onsyn.trisona

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mai_onsyn.trisona.module.AlbumCard
import mai_onsyn.trisona.module.play_bar.PlayBar
import mai_onsyn.trisona.module.SearchInput
import mai_onsyn.trisona.module.content.ContentBox
import mai_onsyn.trisona.module.guid_bar.GuidBox
import mai_onsyn.trisona.module.guid_bar.GuidPage
import mai_onsyn.trisona.module.layout.BackGround
import mai_onsyn.trisona.module.layout.TitleBackBar
import mai_onsyn.trisona.util.ClickableScaleButtonEffect
import mai_onsyn.trisona.util.interaction
import mai_onsyn.trisona.theme.DarkColorTheme
import mai_onsyn.trisona.theme.LightColorTheme
import mai_onsyn.trisona.theme.ThemeInterface
import mai_onsyn.trisona.util.ClickableRoundIconButtonScaleEffect

@Preview
@Composable
fun App() {
    onApplicationStart()
    ThemeInterface(if (Config.isDarkMode) DarkColorTheme else LightColorTheme) {
        val focusManager = LocalFocusManager.current
        BackGround (
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures {
                        focusManager.clearFocus()
                    }
                }
        ) {
            TitleBackBar(
                modifier = Modifier
                    .padding(100.dp, 0.dp, 150.dp, 0.dp)
                    .height(50.dp)
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            ) { titleBarComponents() }

            ContentBox(
                page = GuidPage.CREATES,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 120.dp,
                        end = 50.dp,
                        top = 80.dp,
                        bottom = 80.dp
                    ),
            )

            GuidBox(
                modifier = Modifier
                    .width(100.dp)
                    .height(500.dp)
                    .align(Alignment.CenterStart)
            )

            //main album area
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(
//                        start = 200.dp,
//                        end = 20.dp,
//                        top = 150.dp,
//                        bottom = 20.dp
//                    )
//            ) {
//                Row {
//                    AlbumCard(
////                        coverUrl = "https://p1.music.126.net/3RANMlMM-udSsHyInyVbrQ==/528865105234307.jpg?param=140y140",
//                        onClick = {
//                            Config.isDarkMode = !Config.isDarkMode
//                            println("album card clicked")
//                        }
//                    )
//
//                    Spacer(Modifier.width(16.dp))
//
//                    AlbumCard()
//                }
//            }

            PlayBar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .height(80.dp)
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun BoxScope.titleBarComponents() {
    SearchInput(
        modifier = Modifier
            .align(Alignment.CenterStart)
            .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
            .width(300.dp)
            .fillMaxHeight()
    )

    ClickableRoundIconButtonScaleEffect(
        modifier = Modifier
            .padding(end = 20.dp)
            .align(Alignment.CenterEnd)
            .size(40.dp),
        onClick = { Config.isDarkMode = !Config.isDarkMode }
    ) {
        Text(
            text = "D"
        )
    }
}