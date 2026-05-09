package mai_onsyn.trisona.ui.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.graphics.shapes.RoundedPolygon
import com.alibaba.fastjson2.JSONObject

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

fun flattenJsonObject(jsonObject: JSONObject?): MutableMap<String, String> {
    val result = mutableMapOf<String, String>()
    flatten("", jsonObject!!, result)
    return result
}

private fun flatten(currentPath: String, json: JSONObject, out: MutableMap<String, String>) {
    for (entry in json.entries) {
        val key = entry.key
        val value = entry.value
        val newPath = if (currentPath.isEmpty()) key else "$currentPath.$key"

        if (value is JSONObject) {
            flatten(newPath, value, out)
        } else if (value is String) {
            out[newPath] = value
        }
    }
}

fun Rect.toIntRect(): IntRect {
    return IntRect(
        left = left.toInt(),
        top = top.toInt(),
        right = right.toInt(),
        bottom = bottom.toInt()
    )
}

fun Offset.toIntOffset(): IntOffset {
    return IntOffset(x.toInt(), y.toInt())
}