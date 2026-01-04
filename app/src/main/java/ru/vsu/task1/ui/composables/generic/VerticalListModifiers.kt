package ru.vsu.task1.ui.composables.generic

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.simpleVerticalScrollbar(
    state: ScrollState,
    width: Dp = 8.dp,
    height: Dp = 80.dp,
    color: Color = MaterialTheme.colorScheme.primary
): Modifier {
    val targetAlpha = if (state.isScrollInProgress) 1f else 0f
    val duration = if (state.isScrollInProgress) 150 else 500

    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = duration)
    )

    return drawWithContent {
        drawContent()

        val needDrawScrollbar = state.isScrollInProgress || alpha > 0.0f

        if (needDrawScrollbar) {
            val scrollRatio = state.value.toFloat() / state.maxValue
            val yOffset = (this.size.height - height.toPx()) * scrollRatio

            drawRoundRect(
                color = color,
                topLeft = Offset(this.size.width - width.toPx(), yOffset),
                size = Size(width.toPx(), height.toPx()),
                cornerRadius = CornerRadius(12.dp.toPx()),
                alpha = alpha
            )
        }
    }
}

@Composable
fun Modifier.loadContentWhenListEnding(
    state: ScrollState,
    loadRatio: Float = 0.8f,
    onLoad: () -> Unit = {}
):Modifier {
    val scrollRatio = state.value.toFloat() / state.maxValue
    if (scrollRatio > loadRatio) {
        onLoad()
    }

    return this
}

@Composable
fun Modifier.simpleVerticalScrollbar(
    state: LazyListState,
    width: Dp = 8.dp,
    height: Dp = 80.dp,
    color: Color = MaterialTheme.colorScheme.primary
): Modifier {
    val targetAlpha = if (state.isScrollInProgress) 1f else 0f
    val duration = if (state.isScrollInProgress) 150 else 500

    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = duration)
    )

    return drawWithContent {
        drawContent()

        val needDrawScrollbar = state.isScrollInProgress || alpha > 0.0f

        Log.d("SCROLLBAR", state.firstVisibleItemScrollOffset.toString())


        if (needDrawScrollbar) {
            val itemSize = this.size.height / state.layoutInfo.totalItemsCount
            val scrollRatio = (state.firstVisibleItemIndex * itemSize) / this.size.height
            val yOffset = itemSize * state.firstVisibleItemIndex + state.firstVisibleItemScrollOffset

            drawRoundRect(
                color = color,
                topLeft = Offset(this.size.width - width.toPx(), yOffset),
                size = Size(width.toPx(), height.toPx()),
                cornerRadius = CornerRadius(12.dp.toPx()),
                alpha = alpha
            )
        }
    }
}

@SuppressLint("FrequentlyChangingValue")
@Composable
fun Modifier.loadContentWhenListEnding(
    state: LazyListState,
    loadRatio: Float = 0.8f,
    onLoad: () -> Unit = {}
):Modifier {
    val scrollRatio = state.firstVisibleItemIndex.toFloat() / state.layoutInfo.totalItemsCount
    if (scrollRatio > loadRatio) {
        onLoad()
    }

    return this
}