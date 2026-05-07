package mai_onsyn.trisona.ui.util

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.IntRect
import androidx.graphics.shapes.RoundedPolygon

fun RoundedPolygon.toComposePath(reusedPath: Path = Path()): Path {
    reusedPath.rewind()
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

fun Rect.toIntRect(): IntRect {
    return IntRect(
        left = left.toInt(),
        top = top.toInt(),
        right = right.toInt(),
        bottom = bottom.toInt()
    )
}