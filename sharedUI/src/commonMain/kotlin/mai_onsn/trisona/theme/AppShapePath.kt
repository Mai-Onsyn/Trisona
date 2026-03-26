package mai_onsn.trisona.theme

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.graphics.shapes.RoundedPolygon
import mai_onsn.trisona.module.halfPI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

fun Path.cardPath(size: Size) {
    val clipX = 30f
    val clipY = 40f
    val r = 4f
    val angle = atan2(clipY, clipX)

    reset()
    moveTo(clipX + r, 0f)
    lineTo(size.width - r, 0f)
    quadraticTo(size.width, 0f, size.width, r)
    lineTo(size.width, size.height - clipY - r)
    quadraticTo(size.width, size.height - clipY, size.width - r * cos(angle), size.height - clipY + r * sin(angle))
    lineTo(size.width - clipX + r * cos(angle), size.height - r * sin(angle))
    quadraticTo(size.width - clipX, size.height, size.width - clipX - r, size.height)
    lineTo(r, size.height)
    quadraticTo(0f, size.height, 0f, size.height - r)
    lineTo(0f, clipY + r)
    quadraticTo(0f, clipY, r * sin(halfPI - angle), clipY - r * cos(halfPI - angle))
    lineTo(clipX - r * cos(angle), r * sin(angle))
    quadraticTo(clipX, 0f, clipX + r, 0f)
    close()
}

fun Path.parallelogramPath(size: Size, angle: Float, radius: Float) {
    val h = size.height
    val w = size.width
    val slantWidth = h / tan(angle)

    val ratio = slantWidth / h

    reset()
    moveTo(radius, h)
    lineTo(w - slantWidth - radius, h)
    quadraticTo(
        w - slantWidth, h,
        w - slantWidth + (radius * ratio), h - radius
    )
    lineTo(w - (radius * ratio), radius)
    quadraticTo(
        w, 0f,
        w - radius, 0f
    )
    lineTo(slantWidth + radius, 0f)
    quadraticTo(
        slantWidth, 0f,
        slantWidth - (radius * ratio), radius
    )
    lineTo(radius * ratio, h - radius)
    quadraticTo(
        0f, h,
        radius, h
    )
    close()
}

fun RoundedPolygon.toComposePath(reusedPath: Path = Path()): Path {
    reusedPath.rewind() // 清空旧数据以便复用
    this.cubics.forEachIndexed { index, cubic ->
        if (index == 0) {
            reusedPath.moveTo(cubic.anchor0X, cubic.anchor0Y)
        }
        reusedPath.cubicTo(
            cubic.control0X, cubic.control0Y,
            cubic.control1X, cubic.control1Y,
            cubic.anchor1X, cubic.anchor1Y
        )
    }

    reusedPath.close()
    return reusedPath
}