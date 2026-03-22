package mai_onsyn.trisona

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import mai_onsn.trisona.module.InputAreaBoundInWindow
import java.awt.*
import java.awt.event.MouseEvent
import javax.swing.JFrame

internal const val TITLE_BAR_HEIGHT = 35

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun AppTopBar(title: String, icon: String, window: Window) {
    var mousePressed by remember { mutableStateOf(false) }
    var mousePressedPos by remember { mutableStateOf(Point(0, 0)) }
    var mousePressedWindowPos by remember { mutableStateOf(Point(0, 0)) }

    SideEffect {
        val frame = window as JFrame
        val hwnd = getHWND(frame)

        Toolkit.getDefaultToolkit().addAWTEventListener({ event ->
            if (event is MouseEvent) {
                if (event.isLeftButton() && event.isInTitleBar(frame)) {
                    if (event.isDoubleClick() && event.isEvent(MouseEvent.MOUSE_CLICKED)) {
                        if (frame.extendedState == JFrame.MAXIMIZED_BOTH) {
                            User32.INSTANCE.ShowWindow(hwnd, User32.SW_RESTORE)
                        } else {
                            User32.INSTANCE.ShowWindow(hwnd, User32.SW_MAXIMIZE)
                        }
                    }

                    if (event.isEvent(MouseEvent.MOUSE_PRESSED)) {
                        mousePressed = true
                        mousePressedPos = event.locationOnScreen
                        mousePressedWindowPos = frame.locationOnScreen
                    }

                    if (event.isEvent(MouseEvent.MOUSE_RELEASED)) mousePressed = false
                }

                if (mousePressed) {
                    if (event.isEvent(MouseEvent.MOUSE_DRAGGED)) {
                        val mousePos = MouseInfo.getPointerInfo().location
                        val mouseX = mousePos.x

                        if (frame.extendedState != JFrame.NORMAL) {
                            frame.extendedState = JFrame.NORMAL
                            val windowWidth = window.width
                            val screenWidth = Toolkit.getDefaultToolkit().screenSize.width
                            val c =
                                if (mouseX < screenWidth * 0.7 && mouseX > screenWidth * 0.3) mouseX - windowWidth / 2
                                else if (mouseX < windowWidth * 0.7) 0
                                else if (mouseX > screenWidth - windowWidth * 0.8) screenWidth - windowWidth
                                else mouseX - windowWidth / 2
                            mousePressedWindowPos = Point(c, 0)
                            mousePressedPos = mousePos
                        }

                        frame.setLocation(mousePressedWindowPos.x + mousePos.x - mousePressedPos.x, mousePressedWindowPos.y + mousePos.y - mousePressedPos.y)
                    }
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK or AWTEvent.MOUSE_MOTION_EVENT_MASK)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(TITLE_BAR_HEIGHT.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 12.dp)
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = "Sample",
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(title, color = Color.White, fontSize = 14.sp)
        }
    }
}

private fun MouseEvent.isInTitleBar(frame: JFrame): Boolean {
    val scaleX = frame.graphicsConfiguration.defaultTransform.scaleX
    val scaleY = frame.graphicsConfiguration.defaultTransform.scaleY
    val titleBarHeightPx = TITLE_BAR_HEIGHT * scaleY
    val bounds = InputAreaBoundInWindow
    if (x >= bounds.left / scaleX && x <= bounds.right / scaleX &&
        y >= bounds.top / scaleY && y <= bounds.bottom / scaleY) {
        return false
    }
    return y <= titleBarHeightPx
}

private fun MouseEvent.isDoubleClick(): Boolean = clickCount == 2

private fun MouseEvent.isEvent(type: Int): Boolean = this.id == type

private fun MouseEvent.isLeftButton(): Boolean = button == MouseEvent.BUTTON1

fun getHWND(window: Window): WinDef.HWND {
    val ptr: Pointer = Native.getComponentPointer(window)

    val hwnd = WinDef.HWND(ptr)

    return User32.INSTANCE.GetAncestor(
        hwnd,
        User32.GA_ROOT
    )
}

class DWM_BLURBEHIND(
    @JvmField var dwFlags: WinDef.DWORD,
    @JvmField var fEnable: WinDef.BOOL,
    @JvmField var hRgnBlur: WinDef.HRGN,
    @JvmField var fTransitionOnMaximized: WinDef.BOOL
) : Structure() {
    override fun getFieldOrder() =
        listOf("dwFlags", "fEnable", "hRgnBlur", "fTransitionOnMaximized")
}