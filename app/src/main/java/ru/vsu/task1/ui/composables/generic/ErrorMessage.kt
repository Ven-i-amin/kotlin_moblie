package ru.vsu.task1.ui.composables.generic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.vsu.task1.ui.theme.AppTypography
import ru.vsu.task1.ui.theme.defaultScheme

@Preview
@Composable
fun ErrorMessage(
    modifier: Modifier = Modifier,
    headerMessage: String = "Error",
    errorMessage: String = "Error Message",
    buttonMessage: String = "Restart",
    onClick: () -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme

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

        Button (
            modifier = Modifier
                .clip(RoundedCornerShape(12f))
                .padding(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.error,
                contentColor = colors.onError
            ),
            onClick = onClick,
        ) {
            Text(
                color = defaultScheme.onError,
                text = buttonMessage
            )
        }

    }
}