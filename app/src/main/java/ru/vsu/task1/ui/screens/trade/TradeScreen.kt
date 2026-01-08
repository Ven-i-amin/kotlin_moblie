package ru.vsu.task1.ui.screens.trade

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import ru.vsu.task1.data.models.coin.CoinInfo
import ru.vsu.task1.data.models.home.Order
import ru.vsu.task1.ui.composables.generic.ErrorMessage
import ru.vsu.task1.ui.composables.generic.Loading
import ru.vsu.task1.ui.composables.generic.LoadingView
import ru.vsu.task1.ui.composables.trade.TradeTopBar
import ru.vsu.task1.ui.composables.home.CurrencyPanel
import ru.vsu.task1.ui.composables.trade.CurrencyChart
import ru.vsu.task1.ui.composables.trade.RadioButton
import ru.vsu.task1.ui.composables.trade.RadioButtonRow
import ru.vsu.task1.ui.composables.trade.ValueAndChangeColumn
import ru.vsu.task1.ui.navigation.AppBarViewModel
import ru.vsu.task1.ui.screens.home.TransactionPanel
import ru.vsu.task1.utils.formatDecimal
import ru.vsu.task1.utils.formatPrice

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradeScreen(
    navController: NavController,
    currency: String = "bitcoin",
    tradeSheetState: String?,
    viewModel: TradeViewModel = koinViewModel(),
    appBarViewModel: AppBarViewModel = koinInject()
) {
    val coinInfo by viewModel.coinInfo.collectAsState()
    val isChosen by viewModel.isChosen.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchCoinInfo(currency)
        viewModel.fetchWatchlist()

        if (tradeSheetState != null) {
            val tradeSide = TradeSide.entries.firstOrNull {
                it.value == tradeSheetState
            } ?: return@LaunchedEffect

            viewModel.selectTradeSide(tradeSide)
            viewModel.showTradeSheet()
        }
    }

    LaunchedEffect(coinInfo) {
        appBarViewModel.setTopBar {
            TradeTopBar(
                modifier = Modifier,
                navController = navController,
                coinInfo = coinInfo,
                isInWishlist = isChosen,
                onStarClick = {
                    if (isChosen) {
                        viewModel.removeCoinFromWatchlist(currency)
                    } else {
                        viewModel.addCoinToWatchlist(currency)
                    }

                    viewModel.reverseWatchlist()
                }
            )
        }
        appBarViewModel.hideBottomBar()
    }

    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

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
        MainContent(onPeriodSelected = { days ->
            viewModel.fetchMarketChart(
                coinInfo?.bitgetSymbol ?: "",
                days
            )
        })
    }
}

@Composable
private fun MainContent(
    viewModel: TradeViewModel = koinViewModel(),
    onPeriodSelected: (String) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val coinInfo by viewModel.coinInfo.collectAsState()
    val prices by viewModel.prices.collectAsState()
    val isChartLoading by viewModel.isChartLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val isTradeSheetVisible by viewModel.isTradeSheetVisible.collectAsState()

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
                                style = typography.bodySmall
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
            onClick = { viewModel.showTradeSheet() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Trade",
                style = typography.bodyMedium
            )
        }

        if (isTradeSheetVisible) {
            TradeSheet(
                onDismissRequest = { viewModel.hideTradeSheet() }
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
    val typography = MaterialTheme.typography

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
                        style = typography.bodySmall,
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
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    val orderInfo = order.first
    val isBuy = orderInfo.type.equals("buy", ignoreCase = true)
    val sideLabel = if (isBuy) "Buy" else "Sell"
    val sideColor = if (isBuy) colors.primary else colors.error
    val statusLabel = orderInfo.status.replaceFirstChar { ch ->
        if (ch.isLowerCase()) ch.titlecase() else ch.toString()
    }

    CurrencyPanel(
        icon = order.second?.image,
        iconDescription = orderInfo.currencyName,
        middleColumn = {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Price",
                        style = typography.bodyMedium,
                        color = colors.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = formatPrice(orderInfo.price),
                        style = typography.bodyMedium
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Amount",
                        style = typography.bodyMedium,
                        color = colors.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = formatDecimal(orderInfo.amount),
                        style = typography.bodyMedium
                    )
                }
            }
        },
        rightColumn = {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(sideColor.copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = sideLabel,
                        style = typography.bodyMedium,
                        color = sideColor
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(colors.surface.copy(alpha = 0.7f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = statusLabel,
                        style = typography.bodyMedium,
                        color = colors.onSurface
                    )
                }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TradeSheet(
    viewModel: TradeViewModel = koinViewModel(),
    onDismissRequest: () -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val coinInfo by viewModel.coinInfo.collectAsState()
    val loadingTradeSheet by viewModel.loadingTradeSheet.collectAsState()
    val error by viewModel.error.collectAsState()

    val coinAmount by viewModel.userCoin.collectAsState()
    val balance by viewModel.userBalance.collectAsState()
    val tradeSide by viewModel.tradeSide.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchUserCoins()
    }

    val price = coinInfo?.currentPrice ?: 0.0
    val symbol = coinInfo?.symbol?.uppercase() ?: "CRYPTO"

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var cryptoInput by remember { mutableStateOf("") }
    var fiatInput by remember { mutableStateOf("") }
    var lastEdited by remember { mutableStateOf(InputField.Crypto) }

    val fiatBalance = balance

    fun maxCryptoAllowed(): Double {
        return if (tradeSide == TradeSide.Buy) {
            if (price > 0.0) fiatBalance / price else 0.0
        } else {
            coinAmount
        }
    }

    fun maxFiatAllowed(): Double {
        return if (tradeSide == TradeSide.Buy) {
            fiatBalance
        } else {
            if (price > 0.0) coinAmount * price else 0.0
        }
    }

    fun clampInput(raw: String, maxValue: Double): Pair<String, Double?> {
        val cleaned = normalizeNumberInput(raw)
        if (cleaned.isBlank()) {
            return "" to null
        }
        val amount = cleaned.toDoubleOrNull() ?: return "" to null
        val capped = amount.coerceAtMost(maxValue)
        val text = if (capped != amount) formatDecimal(capped) else cleaned
        return text to capped
    }

    fun updateFiatFromCrypto(raw: String) {
        val (text, amount) = clampInput(raw, maxCryptoAllowed())
        cryptoInput = text
        lastEdited = InputField.Crypto
        fiatInput = if (amount != null && price > 0.0) {
            formatDecimal(amount * price)
        } else {
            ""
        }
    }

    fun updateCryptoFromFiat(raw: String) {
        val (text, amount) = clampInput(raw, maxFiatAllowed())
        fiatInput = text
        lastEdited = InputField.Fiat
        cryptoInput = if (amount != null && price > 0.0) {
            formatDecimal(amount / price)
        } else {
            ""
        }
    }

    LaunchedEffect(price, tradeSide, fiatBalance, coinAmount) {
        if (price <= 0.0) {
            return@LaunchedEffect
        }
        when (lastEdited) {
            InputField.Crypto -> updateFiatFromCrypto(cryptoInput)
            InputField.Fiat -> updateCryptoFromFiat(fiatInput)
        }
    }

    LoadingView(
        isLoading = loadingTradeSheet,
        isError = error != null,
        onLoading = { Loading() },
        onError = { ErrorMessage { viewModel.fetchUserCoins() } }
    ) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            containerColor = colors.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TradeToggle(
                    selectedSide = tradeSide,
                    onSelected = { viewModel.selectTradeSide(it) }
                )

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        value = cryptoInput,
                        onValueChange = { updateFiatFromCrypto(it) },
                        label = { Text("Amount, $symbol") },
                        placeholder = { Text("0") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Text(
                        text = "Available ${formatDecimal(coinAmount)} $symbol",
                        style = typography.labelSmall,
                        color = colors.onSurface.copy(alpha = 0.7f)
                    )

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        value = fiatInput,
                        onValueChange = { updateCryptoFromFiat(it) },
                        label = { Text("Amount, USD") },
                        placeholder = { Text("0") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Text(
                        text = "Available ${formatPrice(fiatBalance)}",
                        style = typography.labelSmall,
                        color = colors.onSurface.copy(alpha = 0.7f)
                    )
                }

                if (price > 0.0) {
                    Text(
                        text = "1 $symbol = ${formatPrice(price)}",
                        style = typography.labelSmall,
                        color = colors.onSurface.copy(alpha = 0.7f)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = {
                        viewModel.addNewOrder(
                            type = if (tradeSide == TradeSide.Buy) "buy" else "sell",
                            price = fiatInput.toDoubleOrNull() ?: 0.0,
                            amount = cryptoInput.toDoubleOrNull() ?: 0.0
                        )

                        viewModel.hideTradeSheet()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        text = if (tradeSide == TradeSide.Buy) "Buy" else "Sell",
                        style = typography.bodyMedium
                    )
                }
            }
        }
    }
}

private const val TOGGLE_ANIMATION_DURATION = 220

@Composable
private fun TradeToggle(
    selectedSide: TradeSide,
    onSelected: (TradeSide) -> Unit
) {
    val colors = MaterialTheme.colorScheme

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(CircleShape)
            .background(colors.surfaceVariant)
    ) {
        val segmentWidth = maxWidth / 2
        val targetOffset = if (selectedSide == TradeSide.Buy) 0.dp else segmentWidth
        val offsetX by animateDpAsState(
            targetValue = targetOffset,
            animationSpec = tween(durationMillis = TOGGLE_ANIMATION_DURATION),
            label = "trade-toggle-offset"
        )

        Box(
            modifier = Modifier
                .offset(x = offsetX)
                .width(segmentWidth)
                .fillMaxHeight()
                .clip(CircleShape)
                .background(colors.primary)
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            TradeToggleButton(
                modifier = Modifier.weight(1f),
                text = "Buy",
                selected = selectedSide == TradeSide.Buy,
                onClick = { onSelected(TradeSide.Buy) }
            )
            TradeToggleButton(
                modifier = Modifier.weight(1f),
                text = "Sell",
                selected = selectedSide == TradeSide.Sell,
                onClick = { onSelected(TradeSide.Sell) }
            )
        }
    }
}

@Composable
private fun TradeToggleButton(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = typography.bodySmall,
            color = if (selected) colors.onPrimary else colors.onSurface
        )
    }
}


private enum class InputField {
    Crypto,
    Fiat
}

private fun normalizeNumberInput(value: String): String {
    var dotSeen = false
    val normalized = value.replace(',', '.')
    return buildString {
        for (ch in normalized) {
            if (ch.isDigit()) {
                append(ch)
            } else if (ch == '.' && !dotSeen) {
                append(ch)
                dotSeen = true
            }
        }
    }
}


