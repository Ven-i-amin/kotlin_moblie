package ru.vsu.task1.composables.generic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.vsu.task1.u1.theme.AppTypography
import ru.vsu.task1.u1.theme.defaultScheme

@Preview
@Composable
fun ErrorMessage(
    modifier: Modifier = Modifier,
    headerMessage: String = "Error",
    errorMessage: String = "Error Message",
    buttonMessage: String = "Restart",
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(bottom = 12.dp),
            color = defaultScheme.error,
            text = headerMessage,
            style = AppTypography.titleLarge
        )

        Text(
            modifier = Modifier.padding(bottom = 12.dp),
            color = defaultScheme.onError,
            text = errorMessage
        )

        RadioButtonButton (
            modifier = Modifier
                .clip(RoundedCornerShape(12f))
                .background(color = defaultScheme.error)
                .padding(5.dp),
            selected = true,
            onClick = onClick,
        ) {
            Text(
                color = defaultScheme.onError,
                text = buttonMessage
            )
        }

    }
}