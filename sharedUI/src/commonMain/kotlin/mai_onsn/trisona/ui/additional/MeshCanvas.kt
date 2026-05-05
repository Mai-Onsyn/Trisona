package mai_onsn.trisona.ui.additional

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import mai_onsn.trisona.tiny.random
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

@Composable
fun DynamicTriangleMesh(
    modifier: Modifier = Modifier,
    triangleSize: Float = 120f,
    maxAlpha: Float = 0.3f,
) {
    val alphas = remember { mutableListOf<Float>() }

    Canvas(modifier = modifier.fillMaxSize()) {
        val height = triangleSize * (sqrt(3f) / 2f)

        val rows = (size.height / height).toInt() + 2
        val cols = (size.width / (triangleSize / 2)).toInt() + 2

        if (alphas.size < rows * cols) {
            alphas.addAll(List(rows * cols) { Random.nextFloat() / 50 })
        }

        var i = 0
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                if (alphas[i] < 0.01f && random.nextInt(20) != 0) {
                    i++
                    continue
                }

                alphas[i] = Ditherer.nextValue(alphas[i], 0f, maxAlpha, 0.005f) {
                    9.31f * exp(-((it / 0.1075f).pow(2)))
                }

                val x = c * (triangleSize / 2f)
                val y = r * height

                val isUpward = (r + c) % 2 == 0
                drawRoundedShrinkTriangle(x - triangleSize, y - triangleSize, triangleSize, height, isUpward, Color.White.copy(alpha = alphas[i++]))
            }
        }
    }
}

fun DrawScope.drawRoundedShrinkTriangle(
    x: Float,
    y: Float,
    size: Float,
    h: Float,
    isUpward: Boolean,
    color: Color,
    padding: Float = 8f,
) {
    if (color.alpha < 0.01f) return
    val path = Path().apply {
        if (isUpward) {
            // 正三角形：缩小边界后的三个顶点
            moveTo(x + size / 2, y + padding) // 顶点
            lineTo(x + padding, y + h - padding) // 左下
            lineTo(x + size - padding, y + h - padding) // 右下
        } else {
            // 倒三角形：缩小边界后的三个顶点
            moveTo(x + padding, y + padding) // 左上
            lineTo(x + size - padding, y + padding) // 右上
            lineTo(x + size / 2, y + h - padding) // 底部顶点
        }
        close()
    }

    drawPath(
        path = path,
        color = color,
        style = Fill
    )
}

object Ditherer {
    private val r = Random

    /**
     * @param value 当前值
     * @param min 最小值
     * @param max 最大值
     * @param step 步长
     * @param f 概率密度函数 (PDF)
     */
    fun nextValue(
        value: Float,
        min: Float,
        max: Float,
        step: Float,
        f: (Float) -> Float
    ): Float {
        val direction = if (r.nextBoolean()) 1f else -1f
        val nextX = value + direction * step

        if (nextX !in min..max) return value

        val pCurrent = f(value)
        val pNext = f(nextX)

        return if (pNext >= pCurrent || r.nextFloat() < (pNext / pCurrent)) { nextX }
        else { value }
    }
}