package mai_onsyn.trisona.ui.additional

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.isActive
import mai_onsyn.trisona.ui.theme.TrisonaTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random
import kotlin.random.asJavaRandom

val random = Random.asJavaRandom()
private val bubbles: List<GasBubble> = mutableListOf(
    GasBubble.mkRandom(x = 0f),
    GasBubble.mkRandom(x = Float.MAX_VALUE)
)

@Composable
fun GasCanvas(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {

        var canvasSize by remember { mutableStateOf(IntSize.Zero) }

        LaunchedEffect(Unit) {
            while (isActive) {
                withFrameNanos {
                    if (canvasSize != IntSize.Zero) {
                        bubbles.forEach {
                            it.update(canvasSize, 500f)
                        }
                    }
                }
            }
        }

        val theme = TrisonaTheme.colorScheme

        Canvas(
            modifier = modifier
                .fillMaxSize()
                .onSizeChanged { intSize ->
                    canvasSize = intSize
                }
        ) {
            bubbles.forEach {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            theme.tip.copy(alpha = 1f), // 圆心完全不透明
                            theme.tip.copy(alpha = 0f)  // 边缘完全透明
                        ),
                        center = it.position.value,
                        radius = it.radius.value,
                    ),
                    radius = it.radius.value,
                    center = it.position.value,
                    blendMode = BlendMode.Screen,
                )
            }
        }
    }
}

class GasBubble(
    var position: MutableState<Offset>,
    var angle: MutableState<Float>, // 当前运动角度（弧度）
    val radius: MutableState<Float>,
    val speed: MutableState<Float>
) {
    fun update(size: IntSize, outRadius: Float, maxRadius: Float = 1500f, minRadius: Float = 1600f) {
        angle.value += (Random.nextFloat() - 0.5f) * 0.4f

        val dx = cos(angle.value) * speed.value
        val dy = sin(angle.value) * speed.value
        var nextX = position.value.x + dx
        var nextY = position.value.y + dy

        if (nextX < -outRadius || nextX > size.width + outRadius || (nextX > 0 && nextX < size.width)) {
            angle.value = Math.PI.toFloat() - angle.value
            nextX = position.value.x
        }
        if (nextY < -outRadius || nextY > size.height + outRadius || (nextY > 0 && nextY < size.height)) {
            angle.value = -angle.value
            nextY = position.value.y
        }

        if (nextX < -outRadius) nextX = -outRadius
        if (nextX > size.width + outRadius) nextX = size.width + outRadius
        if (nextY < -outRadius) nextY = -outRadius
        if (nextY > size.height + outRadius) nextY = size.height + outRadius
        if (nextX > 0 && nextX < size.width) {
            if (nextX > size.width / 2) nextX = size.width.toFloat()
            else nextX = 0f
        }
        if (nextY > 0 && nextY < size.height) {
            if (nextY > size.height / 2) nextY = size.height.toFloat()
            else nextY = 0f
        }

        radius.value += random.nextFloat()
        if (radius.value > maxRadius) radius.value = minRadius
        if (radius.value < minRadius) radius.value = maxRadius

        position.value = Offset(nextX, nextY)
    }

    companion object {
        fun mkRandom(
            x: Float = random.nextFloat(-Float.MAX_VALUE, Float.MAX_VALUE),
            y: Float = random.nextFloat(-Float.MAX_VALUE, Float.MAX_VALUE)
        ): GasBubble {
            return GasBubble(
                mutableStateOf(Offset(x, y)),
                mutableStateOf(
                    random.nextFloat((2 * PI).toFloat())
                ),
                mutableStateOf(
                    random.nextFloat(1500f, 1600f)
                ),
                mutableStateOf(3f)
            )
        }
    }
}