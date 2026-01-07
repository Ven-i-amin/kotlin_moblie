package ru.vsu.task1.ui.composables.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.vsu.task1.ui.composables.generic.SmallImage
import ru.vsu.task1.ui.theme.AppTypography
import ru.vsu.task1.ui.theme.defaultScheme

@Composable
fun CurrencyPanel(
    modifier: Modifier = Modifier,
    icon: String?,
    iconDescription: String,
    middleColumn: @Composable (() -> Unit),
    rightColumn: @Composable (() -> Unit)
) {
    val colors = MaterialTheme.colorScheme
    Row(
        modifier = modifier
            .wrapContentSize()
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(color = colors.secondary)
            .padding(vertical = 12.dp)
            .padding(start = 12.dp)
            .padding(end = 6.dp)
    ) {
        SmallImage(
            modifier = Modifier
                .size(48.dp)
                .padding(end = 12.dp)
                .align(Alignment.CenterVertically),
            url = icon,
            description = iconDescription
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(2f),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                middleColumn()
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                rightColumn()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    MaterialTheme(colorScheme = defaultScheme) {
        val colors = MaterialTheme.colorScheme

        CurrencyPanel(
            icon = null,
            iconDescription = "nothing",
            middleColumn = {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Bought ETH",
                        style = AppTypography.bodyMedium
                    )

                    Text(
                        text = "-100.00",
                        style = AppTypography.bodySmall,
                        color = colors.onSecondary
                    )

                    Text(
                        text = "data",
                        style = AppTypography.bodySmall,
                        color = colors.onSecondary
                    )
                }
            },
            rightColumn = {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "+0.65",
                        style = AppTypography.bodyMedium,
                    )
                }
            }
        )
    }
}