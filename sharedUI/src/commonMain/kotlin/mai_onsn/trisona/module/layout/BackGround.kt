package mai_onsn.trisona.module.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import mai_onsn.trisona.Config
import mai_onsn.trisona.tiny.DynamicTriangleMesh
import mai_onsn.trisona.tiny.GasCanvas
import mai_onsn.trisona.theme.AppColors
import mai_onsn.trisona.theme.LocalAppTheme

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