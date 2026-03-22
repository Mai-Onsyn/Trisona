package mai_onsn.trisona.module

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mai_onsn.trisona.JImage
import mai_onsn.trisona.module.util.cardPath
import mai_onsn.trisona.module.util.interaction
import mai_onsn.trisona.theme.LocalAppTheme
import kotlin.math.PI

@Composable
fun AlbumCard(
    coverUrl: String = "",
    title: String = "Album",
    artist: String = "None",
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .albumCardInteractionEffect(onClick)
            .shadow(
                elevation = 4.dp,
                ambientColor = LocalAppTheme.current.backGroundShadow.copy(alpha = 0.3f),
                spotColor = LocalAppTheme.current.backGroundShadow.copy(alpha = 0.5f),
                shape = CardShape
            )
            .width(120.dp)
            .height(180.dp)

            .clip(CardShape)
            .background(LocalAppTheme.current.albumCardBase)
    ) {
        JImage(
            url = coverUrl,
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop,

            loadingPlaceholder = {
                Box(
                    modifier = Modifier
                        .background(Color.LightGray)
                        .aspectRatio(1f)
                        .fillMaxSize()
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "Loading...",
                    )
                }
            },

            errorPlaceholder = {
                Box(
                    modifier = Modifier
                        .background(Color.LightGray)
                        .aspectRatio(1f)
                        .fillMaxSize()
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "Load Failed",
                    )
                }
            }
        )

        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = title,
                color = LocalAppTheme.current.textBaseColor,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = artist,
//                color = LocalAppTheme.current.textBaseColor,
                fontSize = 10.sp,
                maxLines = 1,
                color = Color.Gray
            )
        }

    }
}

@Composable
fun Modifier.albumCardInteractionEffect(
    onClick: () -> Unit
): Modifier {

    var isHovered by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        if (isPressed) 1f else if (isHovered) 1.08f else 1.04f
    )
    val alpha by animateFloatAsState(
        if (isPressed) 0.45f else if (isHovered) 0.3f else 0f
    )

    val theme = LocalAppTheme.current

    return this
        .interaction(
            onHoveredChange = { isHovered = it },
            onPressedChange = { isPressed = it },
            onClick = { onClick() }
        )
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
            this.shape = CardShape
        }
        .drawWithContent {
            drawContent()
            if (alpha > 0f) {
                val path = Path().apply { cardPath(size) }

                clipPath(path) { drawRect(color = theme.hoverCover.copy(alpha)) }
            }
        }
}

const val halfPI = (PI / 2).toFloat()

val CardShape = GenericShape { size, _ -> cardPath(size) }