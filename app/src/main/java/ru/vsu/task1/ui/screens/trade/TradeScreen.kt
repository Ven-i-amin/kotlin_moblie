package ru.vsu.task1.ui.screens.trade

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import ru.vsu.task1.R
import ru.vsu.task1.domain.models.coin.CoinInfo
import ru.vsu.task1.ui.composables.generic.ErrorMessage
import ru.vsu.task1.ui.composables.generic.Loading
import ru.vsu.task1.ui.composables.generic.LoadingOrErrorMessage
import ru.vsu.task1.ui.composables.generic.RadioButton
import ru.vsu.task1.ui.composables.generic.RadioButtonRow
import ru.vsu.task1.ui.composables.generic.SmallImage
import ru.vsu.task1.ui.composables.trade.CurrencyChart
import ru.vsu.task1.ui.theme.AppTypography
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradeScreen(
    navController: NavController,
    currency: String = "bitcoin",
    viewModel: TradeViewModel = koinViewModel()
) {
    val colors = MaterialTheme.colorScheme

    LaunchedEffect(Unit) {
        viewModel.fetchCoinInfo(currency)
    }

    val coinInfo by viewModel.coinInfo.collectAsState()

    val prices by viewModel.prices.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isChartLoading by viewModel.isChartLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var bell by remember { mutableIntStateOf(0) }
    var star by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (coinInfo?.image != null) {
                                SmallImage(
                                    url = coinInfo?.image?.small,
                                    description = currency
                                )
                            }
                            Text(
                                color = colors.onPrimary,
                                text = coinInfo?.name ?: currency,
                                style = AppTypography.bodyLarge
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { navController.popBackStack() }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_left_24),
                                contentDescription = "Back",
                                tint = colors.onSurface
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { bell = (bell + 1) % 2 }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_bell_24),
                                contentDescription = "Settings",
                                tint = if (bell == 0) colors.onSurface else colors.primary
                            )
                        }

                        IconButton(onClick = { star = (star + 1) % 2 }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_star_24),
                                contentDescription = "Settings",
                                tint = if (star == 0) colors.onSurface else colors.primary
                            )
                        }
                    }
                )
            }
        }
    ) { pv ->
        Box(
            Modifier
                .padding(pv)
                .fillMaxSize()
        ) {
            if (isLoading) {
                return@Box LoadingOrErrorMessage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    isError = error != null,
                    loading = { Loading(modifier = Modifier.padding(horizontal = 170.dp)) },
                    errorMessage = {
                        ErrorMessage {
                            viewModel.fetchCoinInfo(currency)
                        }
                    }
                )
            }

            MainContent(
                coinInfo = coinInfo,
                prices = prices,
                onPeriodSelected = { days ->
                    viewModel.fetchMarketChart(
                        currency,
                        "usd",
                        days.toString()
                    )
                },
                isChartLoading = isChartLoading,
                error = error
            )
        }
    }
}

@Composable
private fun MainContent(
    coinInfo: CoinInfo?,
    prices: List<Float>,
    isChartLoading: Boolean = false,
    error: String?,
    onPeriodSelected: (Int) -> Unit
) {
    val colors = MaterialTheme.colorScheme

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
                        coinInfo?.marketData?.priceChangePercentage24h,
                        coinInfo?.marketData?.priceChange24h,
                    ),
                    color = colors.primary,
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
                                    if (selected) colors.primary
                                    else colors.surface
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
                    error = error,
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
    error: String?,
    onPeriodSelected: (Int) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    var selectedDay by remember { mutableIntStateOf(1) }

    LaunchedEffect(selectedDay) {
        onPeriodSelected(selectedDay)
    }

    Column {
        if (isChartLoading) {
            LoadingOrErrorMessage(
                isError = error != null,
                errorMessage = {
                    ErrorMessage(
                        onClick = { onPeriodSelected(selectedDay) }
                    )
                },
                loading = {
                    Loading(
                        modifier = Modifier.padding(horizontal = 170.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        } else {
            CurrencyChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 6.dp),
                costs = (prices.ifEmpty { listOf(0f, 0f) }).map { it }
            )
        }

        val timePeriods = mapOf("1D" to 1, "1W" to 7, "1M" to 30, "1Y" to 365)

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            timePeriods.forEach { (dayStr, dayNum) ->

                val selected = selectedDay == dayNum

                Box(
                    modifier = Modifier
                        .wrapContentSize(),
                    contentAlignment = Alignment.Center
//                        .padding(vertical = 10.dp, horizontal = 14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background( if (selected) colors.primary else colors.surface )
                            .clip(CircleShape)
                            .clickable { selectedDay = dayNum }
                    ) {
                        Box(modifier = Modifier.padding(18.dp))
                    }

                    Text(
                        text = dayStr,
                        style = AppTypography.bodySmall,
                        color = if (selected) colors.onPrimary else colors.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun TransactionContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(text = "TODO")
    }
}