package ru.vsu.task1.composables.generic

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LoadingOrErrorMessage(
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: @Composable () -> Unit = { ErrorMessage() },
    loading: @Composable () -> Unit,
) {
    Box (
        modifier = modifier
    ) {
        if (isError) {
            return@Box Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment
                    .CenterVertically
                    .plus(Alignment.CenterHorizontally)
            ) {
                errorMessage()
            }
        } else {
            return@Box Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment
                    .CenterVertically
                    .plus(Alignment.CenterHorizontally)
            ) {
                loading()
            }
        }
    }
}