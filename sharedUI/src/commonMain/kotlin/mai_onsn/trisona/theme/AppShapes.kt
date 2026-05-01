package mai_onsn.trisona.theme

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import mai_onsn.trisona.theme.leftSharpParallelogramPath
import kotlin.math.PI

val RightRoundedShape = RoundedCornerShape(
    topStart = 0.dp,
    topEnd = 4.dp,
    bottomStart = 0.dp,
    bottomEnd = 4.dp
)

val GuidButtonShape = GenericShape { size, _ -> leftSharpParallelogramPath(size, (PI/2.25).toFloat(), 4.dp.value) }

val MusicItemShape = GenericShape { size, _ -> parallelogramPath(size, (PI/2.25).toFloat(), 8.dp.value) }