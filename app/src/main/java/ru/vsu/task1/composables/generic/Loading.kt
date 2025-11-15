package ru.vsu.task1.composables.generic

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import ru.vsu.task1.R
import ru.vsu.task1.u1.theme.defaultScheme

@Preview
@Composable
fun Loading(modifier: Modifier = Modifier){
    val rotationDegree = 45
    val totalSteps = 360 / rotationDegree

    var currentStep by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(100)
            currentStep = (currentStep - 1) % totalSteps
        }
    }

    val rotation = currentStep * rotationDegree.toFloat()

    Icon(
        modifier = modifier
            .padding(1.dp)
            .graphicsLayer(rotationZ = rotation)
            .height(100.dp)
            .width(100.dp),
        painter = painterResource(id = R.drawable.ic_loading_24),
        contentDescription = "loading",
        tint = defaultScheme.primary
    )
}