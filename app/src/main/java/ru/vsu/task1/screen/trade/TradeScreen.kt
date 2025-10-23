package ru.vsu.task1.screen.trade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.vsu.task1.R

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.vsu.task1.api.viewmodels.PriceViewModel
import ru.vsu.task1.composables.generic.RadioButtonRow
import ru.vsu.task1.composables.generic.SelectableButton
import ru.vsu.task1.composables.trade.Graphic
import ru.vsu.task1.u1.theme.AppTypography
import ru.vsu.task1.u1.theme.defaultScheme

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun TradeScreen(currency: String = "bitcoin", viewModel: PriceViewModel = PriceViewModel()) {
    val prices by viewModel.prices
    val isLoading by viewModel.isLoading
    val error by viewModel.error

    val chartDays = remember { mutableIntStateOf(1) }

    LaunchedEffect(key1 = chartDays.intValue.inv()) {
        viewModel.fetchMarketChart(
            currency,
            "usdt",
            chartDays.intValue.inv().toString()
        )
    }

    MaterialTheme(colorScheme = defaultScheme) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_bitcoin_24),
                                contentDescription = currency,
                            )
                            Text(
                                color = defaultScheme.onPrimary,
                                text = "crypto_name",
                                style = AppTypography.bodyMedium
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { /* Handle back navigation */ }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_left_24),
                                contentDescription = "Back"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* Handle settings action */ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_bell_24),
                                contentDescription = "Settings",
                                tint = defaultScheme.onSurface
                            )
                        }

                        IconButton(onClick = { /* Handle settings action */ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_star_24),
                                contentDescription = "Settings",
                                tint = defaultScheme.onSurface
                            )
                        }
                    }
                )
            }
        ) { pv ->
            Box(
                Modifier
                    .padding(pv)
                    .fillMaxHeight()
            ) {
                Column(
                    Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxHeight()
                ) {
                    Column(
                        Modifier.padding(vertical = 24.dp)
                    ) {
                        Text(
                            text = "$${if (prices.isNotEmpty()) prices.last() else "None"}",
                            style = AppTypography.displayLarge
                        )
                        Row {
                            Text(
                                text = "procent% ",
                                color = defaultScheme.primary,
                                style = AppTypography.bodyMedium
                            )
                            Text(
                                "Today", style =
                                    AppTypography.bodyMedium
                            )
                        }
                    }

                    listOf("Overview", "Transactions", "Orders").map {
                        val function: @Composable (Boolean) -> Unit = { selected ->
                            SelectableButton(
                                selected = selected,
                                onClick = { /*TODO*/ }
                            ) {
                                Text(
                                    text = it,
                                    style = AppTypography.bodySmall
                                )
                            }
                        }
                        function
                    }.also {
                        RadioButtonRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            onClick = { /*TODO*/ },
                            contents = it
                        )

                        Column {
                            Graphic(prices)

                            mapOf(
                                Pair("1D", 1),
                                Pair("1W", 7),
                                Pair("1M", 30),
                                Pair("1Y", 365)
                            ).map { pair ->
                                val function: @Composable (Boolean) -> Unit = { selected ->
                                    SelectableButton(
                                        selected = selected,
                                        onClick = { chartDays.intValue = pair.value },
                                        contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Text(
                                            text = pair.key,
                                            style = AppTypography.bodySmall
                                        )
                                    }
                                }
                                function
                            }.also {
                                RadioButtonRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    onClick = { /*TODO*/ },
                                    contents = it
                                )
                            }
                        }

                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp),
                            onClick = { /*TODO*/ }
                        ) {
                            Text(
                                text = "Trade",
                                style = AppTypography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}