package ru.vsu.task1.ui.screens.trade

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import ru.vsu.task1.domain.models.coin.CoinInfo
import ru.vsu.task1.domain.models.home.Order
import ru.vsu.task1.ui.composables.generic.ErrorMessage
import ru.vsu.task1.ui.composables.generic.Loading
import ru.vsu.task1.ui.composables.generic.LoadingView
import ru.vsu.task1.ui.composables.generic.RadioButton
import ru.vsu.task1.ui.composables.generic.RadioButtonRow
import ru.vsu.task1.ui.composables.generic.topbar.TradeTopBar
import ru.vsu.task1.ui.composables.home.CurrencyPanel
import ru.vsu.task1.ui.composables.trade.CurrencyChart
import ru.vsu.task1.ui.composables.trade.ValueAndChangeColumn
import ru.vsu.task1.ui.navigation.AppBarViewModel
import ru.vsu.task1.ui.screens.home.TransactionPanel
import ru.vsu.task1.ui.theme.AppTypography
import ru.vsu.task1.utils.formatDecimal
import ru.vsu.task1.utils.formatPrice

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradeScreen(
    navController: NavController,
    currency: String = "bitcoin",
    viewModel: TradeViewModel = koinViewModel(),
    appBarViewModel: AppBarViewModel = koinInject()
) {
    val coinInfo by viewModel.coinInfo.collectAsState()
    val watchlistOn by viewModel.watchlistOn.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchCoinInfo(currency)
        viewModel.fetchWatchlist()
    }

    LaunchedEffect(watchlistOn) {
        appBarViewModel.setTopBar {
            TradeTopBar(
                modifier = Modifier,
                navController = navController,
                coinInfo = coinInfo,
                isStarred = watchlistOn,
                onStarClick = {
                    viewModel.fetchWatchlist()
                }
            )
        }
        appBarViewModel.hideBottomBar()
    }

    val prices by viewModel.prices.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isChartLoading by viewModel.isChartLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val chartError by viewModel.chartError.collectAsState()

    LoadingView(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        isLoading = isLoading,
        isError = error != null,
        onLoading = { Loading(modifier = Modifier.padding(horizontal = 170.dp)) },
        onError = {
            ErrorMessage {
                viewModel.fetchCoinInfo(currency)
            }
        }
    ) {
        MainContent(
            coinInfo = coinInfo,
            prices = prices,
            onPeriodSelected = { days: String ->
                viewModel.fetchMarketChart(
                    coinInfo?.bitgetSymbol ?: "",
                    days
                )
            },
            isChartLoading = isChartLoading,
            error = chartError
        )
    }
}

@Composable
private fun MainContent(
    coinInfo: CoinInfo?,
    prices: List<Float>,
    isChartLoading: Boolean = false,
    error: String?,
    onPeriodSelected: (String) -> Unit
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
        ValueAndChangeColumn(
            modifier = Modifier.padding(vertical = 24.dp),
            value = coinInfo?.currentPrice,
            change = coinInfo?.priceChange24h,
            percentageChange = coinInfo?.priceChangePercentage24h,
            overall = "Today"
        )

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
                "orders" -> OrderContent()
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
    onPeriodSelected: (String) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    var selectedDay by remember { mutableStateOf("3min") }

    LaunchedEffect(selectedDay) {
        onPeriodSelected(selectedDay)
    }

    Column {
        LoadingView(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 6.dp),
            isLoading = isChartLoading,
            isError = error != null && !isChartLoading,
            onLoading = {
                Loading(
                    modifier = Modifier.padding(horizontal = 170.dp)
                )
            },
            onError = {
                ErrorMessage(
                    onClick = { onPeriodSelected(selectedDay) }
                )
            }
        ) {
            CurrencyChart(
                modifier = Modifier,
                prices = (prices.ifEmpty { listOf(0f, 0f) }).map { it }
            )
        }

        val timePeriods = mapOf("1D" to "3min", "1W" to "4h", "1M" to "6h", "1Y" to "1day")

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
                            .background(if (selected) colors.primary else colors.surface)
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
private fun TransactionContent(
    viewModel: TradeViewModel = koinViewModel(),
) {
    val typography = MaterialTheme.typography

    val transactions by viewModel.transaction.collectAsState()
    val isLoading by viewModel.loadingTransactions.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchTransaction()
    }

    LoadingView(
        isLoading = isLoading,
        isError = error != null,
        onLoading = { Loading() },
        onError = { ErrorMessage { viewModel.fetchTransaction() } }
    ) {
        Column(
            modifier = Modifier.padding(top = 12.dp)
        ) {
            if (transactions.isEmpty()) {
                return@LoadingView Text(
                    text = "You hadn't traded this coin yet!",
                    style = typography.titleLarge
                )
            }

            Column(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                transactions.forEach {
                    TransactionPanel(it)
                }
            }
        }
    }
}

@Composable
private fun OrderPanel(order: Pair<Order, CoinInfo?>) {
    val typography = MaterialTheme.typography

    CurrencyPanel(
        icon = order.second?.image,
        iconDescription = order.first.currencyName,
        middleColumn = {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = order.first.type,
                    style = typography.bodyMedium
                )

                Text(
                    text = formatPrice(order.first.price),
                    style = typography.bodyMedium
                )

                Text(
                    text = formatDecimal(order.first.amount),
                    style = typography.bodyMedium
                )
            }
        },
        rightColumn = {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = order.first.status,
                    style = typography.bodyMedium,
                )
            }
        }
    )
}

@Composable
private fun OrderContent(
    viewModel: TradeViewModel = koinViewModel(),
) {
    val typography = MaterialTheme.typography

    val coinInfo by viewModel.coinInfo.collectAsState()
    val orders by viewModel.orders.collectAsState()
    val isLoading by viewModel.loadingOrders.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchOrders()
    }

    LoadingView(
        isLoading = isLoading,
        isError = error != null,
        onLoading = { Loading() },
        onError = { ErrorMessage { viewModel.fetchOrders() } }
    ) {
        Column(
            modifier = Modifier.padding(top = 12.dp)
        ) {
            if (orders.isEmpty()) {
                return@LoadingView Text(
                    text = "You don't have any orders!",
                    style = typography.titleLarge
                )
            }

            Column(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                orders.forEach { order ->
                    OrderPanel(order to coinInfo)
                }
            }
        }
    }
}

@Composable
private fun TradePanel() {

}