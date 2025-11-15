package ru.vsu.task1.screen.trade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.vsu.task1.R
import ru.vsu.task1.api.viewmodels.trade.TradeScreenViewModel
import ru.vsu.task1.composables.generic.ErrorMessage
import ru.vsu.task1.composables.generic.Loading
import ru.vsu.task1.composables.generic.RadioButton
import ru.vsu.task1.composables.generic.RadioButtonRow
import ru.vsu.task1.composables.trade.CurrencyChart
import ru.vsu.task1.u1.theme.AppTypography
import ru.vsu.task1.u1.theme.defaultScheme
import java.util.Locale
import kotlin.collections.ifEmpty
import kotlin.collections.map

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun TradeScreen(
    currency: String = "bitcoin",
    viewModel: TradeScreenViewModel = viewModel()
) {
    val coinName by viewModel.coinName
    val coinImage by viewModel.coinImage
    val coinChange24h by viewModel.coinChange24h
    val coinChangePercentage24h by viewModel.coinChangePercentage24h

    val prices by viewModel.prices
    val isLoading by viewModel.isLoading
    val isChartLoading by viewModel.isChartLoading
    val error by viewModel.error

    var bell by remember { mutableIntStateOf(0) }
    var star by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.fetchCoinInfo(currency)
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
                            AsyncImage(
                                modifier = Modifier.height(32.dp),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(coinImage)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = currency,
                                placeholder = painterResource(id = R.drawable.ic_bitcoin_24),
                                contentScale = ContentScale.Fit
                            )
                            Text(
                                color = defaultScheme.onPrimary,
                                text = coinName ?: "Not enabled",
                                style = AppTypography.bodyLarge
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
                        IconButton(onClick = { bell = (bell + 1) % 2 }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_bell_24),
                                contentDescription = "Settings",
                                tint = if (bell == 0) defaultScheme.onSurface else defaultScheme.primary
                            )
                        }

                        IconButton(onClick = { star = (star + 1) % 2 }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_star_24),
                                contentDescription = "Settings",
                                tint = if (star == 0) defaultScheme.onSurface else defaultScheme.primary
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
                if (error != null) {
                    return@Box Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        contentAlignment = Alignment
                            .CenterVertically
                            .plus(Alignment.CenterHorizontally)
                    ) {
                        ErrorMessage()
                    }
                } else if (isLoading) {
                    return@Box Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        contentAlignment = Alignment
                            .CenterVertically
                            .plus(Alignment.CenterHorizontally)
                    ) {
                        Loading(modifier = Modifier.padding(horizontal = 170.dp))
                    }
                }

                MainContent(
                    coinChange24h = coinChange24h,
                    coinChangePercentage24h = coinChangePercentage24h,
                    prices = prices,
                    onPeriodSelected = { days ->
                        viewModel.fetchMarketChart(
                            currency,
                            "usd",
                            days.toString()
                        )
                    },
                    isChartLoading = isChartLoading
                )
            }
        }
    }
}

@Composable
private fun MainContent(
    coinChange24h: Double?,
    coinChangePercentage24h: Double?,
    prices: List<Float>,
    isChartLoading: Boolean = false,
    onPeriodSelected: (Int) -> Unit
) {
    var content by remember {
        mutableStateOf("chart")
    }

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
                    text = String.format(
                        Locale.US,
                        "%+.2f%% ($%.2f) ",
                        coinChangePercentage24h,
                        coinChange24h,
                    ),
                    color = defaultScheme.primary,
                    style = AppTypography.bodyMedium
                )
                Text(
                    "Today", style =
                        AppTypography.bodyMedium
                )
            }
        }

        val buttonList = mapOf(
            "Overview" to "chart",
            "Transactions" to "transaction",
            "Orders" to "orders"
        )

        RadioButtonRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            contents = buttonList.map {
                RadioButton(
                    selectableComposable = @Composable
                    { selected: Boolean ->
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(
                                    if (selected) defaultScheme.primary
                                else defaultScheme.surface
                                )
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(10.dp),
                                text = it.key,
                                style = AppTypography.bodySmall
                            )
                        }
                    },
                    onClick = { content = it.value }
                )
            }
        )

        Box(modifier = Modifier.weight(1f)) {
            when (content) {
                "chart" -> ChartContent(
                    prices = prices,
                    isChartLoading = isChartLoading,
                    onPeriodSelected = onPeriodSelected
                )

                "transaction" -> TransactionContent()
                "orders" -> TransactionContent()
            }
        }

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Trade",
                style = AppTypography.bodyMedium
            )
        }
    }
}

@Composable
private fun ChartContent(
    prices: List<Float>,
    isChartLoading: Boolean,
    onPeriodSelected: (Int) -> Unit
) {
    Column {
        if (isChartLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .weight(1f),
                contentAlignment = Alignment
                    .CenterVertically
                    .plus(Alignment.CenterHorizontally)
            ) {
                Loading(modifier = Modifier)
            }
        } else {
            CurrencyChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 6.dp),
                costs = (prices.ifEmpty { listOf(1000f, 1299f, 1100f, 1040f) }).map { it }
            )
        }

        val timePeriods = mapOf("1D" to 1, "1W" to 7, "1M" to 30, "1Y" to 365)

        RadioButtonRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            contents = timePeriods.map {
                RadioButton(
                    selectableComposable = @Composable
                    { selected: Boolean ->
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(
                                    if (selected) defaultScheme.primary
                                    else defaultScheme.surface
                                )
                        ) {
                            Box(modifier = Modifier.padding(18.dp))
                        }

                        Text(
                            modifier = Modifier
                                .padding(10.dp),
                            text = it.key,
                            style = AppTypography.bodySmall
                        )
                    },
                    onClick = { onPeriodSelected(it.value) }
                )
            }
        )
    }
}

@Composable
private fun TransactionContent() {
    Box(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
    ) {
        Text(text = "TODO")
    }
}