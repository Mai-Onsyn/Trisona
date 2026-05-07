package mai_onsyn.trisona.module.layout

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TipArea(
    tip: String,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable () -> Unit
) {
    PopupBox(
        modifier = modifier,
        popupShape = RoundedCornerShape(4.dp),
        popupAlignment = Alignment.TopStart,
        clickTrigger = false,
        triggerDelay = 500,
        followCursor = true,
        interactionContentAlignment = contentAlignment,
        cursorOffset = IntOffset(0, -35),
        interactionContent = { SelectionContainer { content() } }
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