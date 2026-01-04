package ru.vsu.task1.ui.composables.generic

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
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
import ru.vsu.task1.ui.theme.defaultScheme

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

    Box(modifier = modifier) {
        Icon(
            modifier = Modifier
                .padding(1.dp)
                .graphicsLayer(rotationZ = rotation)
                .sizeIn(1.dp, 1.dp, maxWidth = 20.dp, maxHeight = 20.dp),
            painter = painterResource(id = R.drawable.ic_loading_24),
            contentDescription = "loading",
            tint = defaultScheme.primary
        )
    }
}