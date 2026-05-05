package mai_onsn.trisona.ui.util

import androidx.compose.ui.graphics.Path
import androidx.graphics.shapes.RoundedPolygon

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