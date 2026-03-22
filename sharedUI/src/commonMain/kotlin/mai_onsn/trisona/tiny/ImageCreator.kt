package mai_onsn.trisona.tiny

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint

fun createPixelImage(width: Int, height: Int): ImageBitmap {
    val bitmap = ImageBitmap(width, height)

    val canvas = Canvas(bitmap)
    val paint = Paint()

    for (x in 0 until width) {
        for (y in 0 until height) {
            paint.color = Color(
                red = x.toFloat() / width,
                green = y.toFloat() / height,
                blue = 0.5f,
                alpha = 1.0f
            )
            canvas.drawRect(
                left = x.toFloat(),
                top = y.toFloat(),
                right = x.toFloat() + 1f,
                bottom = y.toFloat() + 1f,
                paint = paint
            )
        }
    }

    return bitmap
}