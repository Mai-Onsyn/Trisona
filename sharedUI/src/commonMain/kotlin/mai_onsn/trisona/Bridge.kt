package mai_onsn.trisona

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mai_onsyn.trisona.core.network.common.ImageLoader

@Composable
fun JImage(
    url: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = 1f,
    colorFilter: ColorFilter? = null,
    loadingPlaceholder: @Composable () -> Unit = {}, // 加载时的占位
    errorPlaceholder: @Composable () -> Unit = {}    // 失败时的占位
) {
    var bitmap by remember(url) { mutableStateOf<ImageBitmap?>(null) }
    var isLoading by remember(url) { mutableStateOf(true) }

    LaunchedEffect(url) {
        if (url.isNullOrEmpty()) {
            isLoading = false
            return@LaunchedEffect
        }
        isLoading = true
        withContext(Dispatchers.IO) {
            val bufferedImage = ImageLoader.fromSync(url)
            bitmap = bufferedImage?.toComposeImageBitmap()
            isLoading = false
        }
    }

    Box(modifier = modifier) {
        val currentBitmap = bitmap
        if (currentBitmap != null) {
            Image(
                modifier = modifier,
                bitmap = currentBitmap,
                contentDescription = contentDescription,
                alignment = alignment,
                contentScale = contentScale,
                alpha = alpha,
                colorFilter = colorFilter
            )
        } else if (isLoading) {
            loadingPlaceholder()
        } else {
            errorPlaceholder()
        }
    }
}