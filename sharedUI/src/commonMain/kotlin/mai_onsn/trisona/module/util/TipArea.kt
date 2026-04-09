package mai_onsn.trisona.module.util

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import mai_onsn.trisona.module.layout.PopupBox

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TipArea(
    tip: String,
    content: @Composable () -> Unit
) {
//    var lastTime by remember { mutableStateOf(System.currentTimeMillis()) }
//    var showPopup by remember { mutableStateOf(false) }
//    ToolTipManager.register { showPopup = System.currentTimeMillis() - lastTime > 1000 }

    PopupBox(
//        modifier = Modifier
//            .interaction(
//                onMove = {
//                    lastTime = System.currentTimeMillis()
//                }
//            ),
        popupShape = RoundedCornerShape(4.dp),
        popupAlignment = Alignment.TopStart,
        followCursor = true,
        cursorOffset = IntOffset(0, -35),
        interactionContent = content
    ) {
        Box(
            modifier = Modifier
                .background(Color(0x7f000000))
                .padding(4.dp)
        ) {
            Text(
                text = tip,
                color = Color.White,
                fontSize = 12.sp
            )
        }
    }
}

//object ToolTipManager {
//    private val activeAreas = mutableListOf<() -> Unit>()
//
//    fun register(f: () -> Unit) { activeAreas.add(f) }
//    fun unregister(f: () -> Unit) { activeAreas.remove(f) }
//
//    @Composable
//    fun GlobalMonitor() {
//        LaunchedEffect(Unit) {
//            while (true) {
//                try {
//                    activeAreas.forEach { it.invoke() }
//                } catch (_: Exception) {}
//                delay(100)
//            }
//        }
//    }
//}