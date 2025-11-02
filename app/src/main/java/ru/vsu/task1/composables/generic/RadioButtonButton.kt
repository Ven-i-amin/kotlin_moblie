package ru.vsu.task1.composables.generic

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RadioButtonButton(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    if (selected) {
        onClick.invoke()
    }

    Box(
        modifier = modifier
    ) {
        content()
    }
}