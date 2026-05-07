package mai_onsyn.trisona.util

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import mai_onsyn.trisona.Config.animationBaseRate

val tweenSpecFloat = tween<Float>(durationMillis = animationBaseRate, easing = LinearOutSlowInEasing)
val tweenSpecDp = tween<Dp>(durationMillis = animationBaseRate, easing = LinearOutSlowInEasing)
val tweenSpecColor = tween<Color>(durationMillis = animationBaseRate, easing = LinearOutSlowInEasing)