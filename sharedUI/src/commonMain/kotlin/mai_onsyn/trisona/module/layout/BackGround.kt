package mai_onsyn.trisona.module.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import mai_onsyn.trisona.Config
import mai_onsyn.trisona.tiny.DynamicTriangleMesh
import mai_onsyn.trisona.tiny.GasCanvas
import mai_onsyn.trisona.theme.AppColors
import mai_onsyn.trisona.theme.LocalAppTheme

@Composable
fun BackGround(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LocalAppTheme.current.backgroundColor),
    ) {
//        GasCanvas(modifier)
//        DynamicTriangleMesh(modifier)

        content()
    }
}