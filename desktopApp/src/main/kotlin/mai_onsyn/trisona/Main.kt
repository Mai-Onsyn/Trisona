package mai_onsyn.trisona

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.jetbrains.JBR
import mai_onsyn.trisona.ui.App
import mai_onsyn.trisona.ui.util.CoreState
import java.awt.Dimension

fun main() = application {
//    System.setProperty("skiko.renderApi", "VULKAN")
//    System.setProperty("skiko.fps.enabled", "false")
    val windowState = rememberWindowState(width = 1024.dp, height = 680.dp)
    Window(
        title = "Trisona",
        state = windowState,
        onCloseRequest = ::exitApplication,
        undecorated = false,
        transparent = false,
        icon = painterResource("icon.png"),
        onKeyEvent = { keyEvent ->
            if (keyEvent.key == Key.Spacebar && keyEvent.type == KeyEventType.KeyDown) {
                CoreState.togglePlay()
//                pauseCallback?.invoke()
//                println(pauseCallback == null)
//                println("space pressed")
            }
            false
        }
    ) {
        val trayState = rememberTrayState()
        Tray(
            state = trayState,
            icon = painterResource("icon.png"),
            menu = {
//                Item("exit", onClick = ::exitApplication)
            }
        )
        window.minimumSize = Dimension(640, 480)

        if (JBR.isAvailable()) {
            val decorations = JBR.getWindowDecorations()
            val customTitleBar = decorations.createCustomTitleBar()
            customTitleBar.height = TITLE_BAR_HEIGHT.toFloat()
            JBR.getWindowDecorations().setCustomTitleBar(window, customTitleBar)
        }

        Box(modifier = Modifier.fillMaxSize()
            .pointerInput(Unit) {}
        ) {
            App()
            AppTopBar(
                window = window,
                title = window.title,
                icon = "icon.png",
            )
        }
    }
}