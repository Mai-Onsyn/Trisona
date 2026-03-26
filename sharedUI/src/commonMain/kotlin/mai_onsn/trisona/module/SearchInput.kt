package mai_onsn.trisona.module

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mai_onsn.trisona.theme.parallelogramPath
import mai_onsn.trisona.theme.AppColors
import mai_onsn.trisona.theme.LocalAppTheme
import org.jetbrains.compose.resources.painterResource
import trisona.sharedui.generated.resources.Res
import trisona.sharedui.generated.resources.icon_search
import kotlin.math.PI

var InputAreaBoundInWindow = Rect.Zero

@Composable
fun SearchInput(
    modifier: Modifier = Modifier
) {

    val theme = LocalAppTheme.current

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val bgScale by animateFloatAsState(
        if(isPressed) 0.94f else if (isFocused) 1.04f else 1f
    )

    Box(modifier = modifier
        .scale(bgScale)
        .border(
            width = 1.dp,
            color = AppColors.White.copy(0.1f),
            shape = SearchInputShape
        )
        .clip(SearchInputShape)
        .background(theme.buttonBaseColor)
    ) {

        var text by remember { mutableStateOf("") }

        BasicTextField(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 16.dp)
                .onGloballyPositioned { coordinates ->
                    InputAreaBoundInWindow = coordinates.boundsInWindow()
                },
            value = text,
            onValueChange = { text = it },
            singleLine = true,
            cursorBrush = SolidColor(theme.textBaseColor),
            interactionSource = interactionSource,
            textStyle = TextStyle(
                color = theme.textBaseColor,
                fontSize = 15.sp
            ),
            decorationBox = { innerTextField ->

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(Res.drawable.icon_search),
                        contentDescription = null,
                        tint = theme.iconMain,
                        modifier = Modifier
                            .size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (text.isEmpty()) {
                            Text(
                                text = "Search",
                                color = theme.textPromptColor.copy(alpha = 0.5f),
                            )
                        }
                        innerTextField()
                    }
                }
            }
        )
    }
}

val SearchInputShape = GenericShape { size, _ -> parallelogramPath(size, (PI/2.25).toFloat(), 4f) }
