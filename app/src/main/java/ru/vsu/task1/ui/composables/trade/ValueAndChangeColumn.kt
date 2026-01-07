package ru.vsu.task1.ui.composables.trade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vsu.task1.utils.formatPercentage
import ru.vsu.task1.utils.formatPrice

@Composable
fun ValueAndChangeColumn(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    value: Double?,
    change: Double?,
    percentageChange: Double?,
    overall: String
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    val changeText = "${formatPercentage(percentageChange)} (${formatPrice(change)}) "

    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        Text(
            text = formatPrice(value),
            style = typography.displayLarge
        )

        Row {
            Text(
                text = changeText,
                color = colors.primary,
                style = typography.bodyMedium
            )
            Text(
                text = overall,
                style = typography.bodyMedium
            )
        }
    }
}