package ru.vsu.task1.ui.composables.generic

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LoadingView(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isError: Boolean = false,
    onLoading: @Composable () -> Unit,
    onError: @Composable () -> Unit = { ErrorMessage() },
    onSuccess: @Composable () -> Unit = {}
) {
    Box (
        modifier = modifier
    ) {
        if (isError) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment
                    .CenterVertically
                    .plus(Alignment.CenterHorizontally)
            ) {
                onError()
            }
        } else if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment
                    .CenterVertically
                    .plus(Alignment.CenterHorizontally)
            ) {
                onLoading()
            }
        } else {
            onSuccess()
        }
    }
}