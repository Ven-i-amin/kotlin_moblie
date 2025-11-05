package ru.vsu.task1.composables.generic

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import ru.vsu.task1.R
import ru.vsu.task1.u1.theme.defaultScheme

@Preview
@Composable
fun Loading(modifier: Modifier = Modifier){
    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing)
        ),
        label = "rotation"
    )

    Icon(
        modifier = modifier.graphicsLayer(rotationZ = rotation),
        painter = painterResource(id = R.drawable.ic_loading_24),
        contentDescription = "loading",
        tint = defaultScheme.primary
    )
}