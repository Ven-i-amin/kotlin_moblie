package ru.vsu.task1.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import ru.vsu.task1.R
import ru.vsu.task1.api.models.home.Transaction
import ru.vsu.task1.api.models.trade.CoinInfo
import ru.vsu.task1.api.viewmodels.home.HomeViewModel
import ru.vsu.task1.composables.generic.ErrorMessage
import ru.vsu.task1.composables.generic.Loading
import ru.vsu.task1.composables.generic.LoadingOrErrorMessage
import ru.vsu.task1.composables.home.CurrencyPanel
import ru.vsu.task1.u1.theme.AppTypography
import ru.vsu.task1.u1.theme.defaultScheme
import java.util.Locale
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.fetchBalance()
        viewModel.fetchWatchlist()
        viewModel.fetchUserTransactions()
    }

    val isLoadingTransaction by viewModel.isLoadingTransactions.collectAsState()
    val isLoadingCoins by viewModel.isLoadingCoins.collectAsState()
    val error by viewModel.error.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val coins by viewModel.watchlistCoins.collectAsState()
    val balance by viewModel.balance.collectAsState()

    MaterialTheme(colorScheme = defaultScheme) {
        Scaffold(
            topBar = {
                Column(
                    modifier = Modifier
                ) {
                    TopAppBar(
                        title = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    color = defaultScheme.onPrimary,
                                    text = "Home",
                                    style = AppTypography.bodyLarge
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = { /* Handle back navigation */ }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_hamburger_24),
                                    contentDescription = "Back"
                                )
                            }
                        },
                        actions = {
                            Box(modifier = Modifier.fillMaxWidth(0.1f))
                        }
                    )
                }
            },
            bottomBar = {
                var selected by remember { mutableIntStateOf(0) }

                NavigationBar(
                    containerColor = defaultScheme.background,

                ) {
                    val icons = listOf(
                        R.drawable.ic_home_24,
                        R.drawable.ic_portfolio_24,
                        R.drawable.ic_trade_24,
                        R.drawable.ic_account_24
                    )

                    icons.forEachIndexed { index, icon ->
                        NavigationBarItem(
                            selected = selected == index,
                            onClick = { selected = index },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = defaultScheme.primary,
                                indicatorColor = Color.Transparent,
                                unselectedIconColor = defaultScheme.onSurface
                            ),
                            icon = {
                                Icon(
                                    painter = painterResource(id = icon),
                                    contentDescription = "Home"
                                )
                            }
                        )
                    }
                }
            }
        ) { pv ->
            Box(
                Modifier
                    .padding(pv)
                    .fillMaxHeight()
            ) {
                if (isLoadingCoins || isLoadingTransaction) {
                    return@Box LoadingOrErrorMessage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        isError = error != null,
                        errorMessage = { ErrorMessage(
                            errorMessage = error ?: "none",
                        ) {
                            viewModel.fetchBalance()
                            viewModel.fetchWatchlist()
                            viewModel.fetchUserTransactions()
                        }},
                        loading = { Loading(modifier = Modifier.padding(horizontal = 170.dp)) }
                    )
                }


                MainContent(
                    navController = navController,
                    transactions = transactions,
                    coins = coins,
                    balance = balance
                )
            }
        }
    }
}

@Composable
private fun MainContent(
    navController: NavController,
    transactions: Map<Transaction, CoinInfo>,
    coins: List<CoinInfo>,
    balance: Double?
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp)
    ) {
        BalanceAndPurchaseButtons(balance = balance)


        TransactionList(
            navController = navController,
            transactions = transactions
        )

        Watchlist(
            navController = navController,
            coins = coins
        )
    }
}

@Composable
private fun BalanceAndPurchaseButtons(
    balance: Double?
) {
    Column(
        modifier = Modifier.padding(bottom = 24.dp)
    ) {
        Column(
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Text(
                modifier = Modifier.padding(bottom = 12.dp),
                text = "Your balance",
                style = AppTypography.bodyLarge
            )
            Text(
                text = "$${balance ?: "Not avaible"}",
                style = AppTypography.displayLarge
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val buttons = mapOf(
                "Top up" to R.drawable.ic_up_icon_24,
                "Buy" to R.drawable.ic_plus_24,
                "Sell" to R.drawable.ic_minus_24
            )

            buttons.forEach { (text, icon) ->
                Button(
                    modifier = Modifier
//                            .padding(horizontal = 8.dp)
                        .aspectRatio(1f)
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = defaultScheme.onSecondary.copy(0.33f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(24.dp),
                    onClick = { /* Handle button click */ },
                ) {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(
                            modifier = Modifier.background(color = Color.Transparent),
                            painter = painterResource(id = icon),
                            contentDescription = "arrow",
                        )

                        Text(
                            modifier = Modifier.background(color = Color.Transparent),
                            text = text,
                            style = AppTypography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionList(
    navController: NavController,
    transactions: Map<Transaction, CoinInfo>
) {
    var isAllTransactions by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(bottom = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Transactions",
                style = AppTypography.bodyMedium
            )

            Button(
                modifier = Modifier.padding(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                contentPadding = PaddingValues(0.dp),
                onClick = { isAllTransactions = !isAllTransactions }
            ) {
                Text(
                    text = if (isAllTransactions) "Close all" else "See all",
                    color = defaultScheme.primary,
                    style = AppTypography.bodyMedium
                )
            }
        }

        if (transactions.isEmpty()) {
            return Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No transactions",
                    style = AppTypography.bodyMedium,
                    color = defaultScheme.onSurface
                )
            }
        }

        TransactionPanel(transactions.asIterable().first())

        AnimatedVisibility(
            visible = isAllTransactions,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(
                modifier = Modifier.padding(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                transactions.asIterable().drop(1).forEach {
                    TransactionPanel(it)
                }
            }
        }
    }
}

@Composable
private fun Watchlist(
    navController: NavController,
    coins: List<CoinInfo>,
) {
    Column {
        Row(
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Text(
                text = "Watchlist",
                style = AppTypography.bodyMedium
            )
        }

        if (coins.isEmpty()) {
            return Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No coins in watchlist",
                    style = AppTypography.bodyMedium,
                    color = defaultScheme.onSurface
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            for (coin in coins) {
                CurrencyPanel(
                    modifier = Modifier.clickable(
                        onClick = {navController.navigate("trade/${coin.id}")}
                    ),
                    icon = coin.image?.small,
                    iconDescription = coin.name,
                    middleColumn = {
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = coin.name,
                                style = AppTypography.bodyMedium
                            )

                            Text(
                                text = coin.symbol.uppercase(),
                                style = AppTypography.bodyMedium,
                                color = defaultScheme.onSecondary
                            )
                        }
                    },
                    rightColumn = {
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = String.format(
                                    Locale.US,
                                    "$%.2f",
                                    coin.marketData?.currentPrice?.get("usd") ?: "Not avaible"
                                ),
                                style = AppTypography.bodyMedium,
                            )

                            val percentageChange = coin.marketData?.priceChangePercentage24h ?: 0.0

                            Text(
                                text = String.format(
                                    Locale.US,
                                    "%s%.2f%%",
                                    if (percentageChange >= 0) "+" else "-",
                                    abs(percentageChange)

                                ),
                                style = AppTypography.bodySmall,
                                color = defaultScheme.primary
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun TransactionPanel(transaction: Map.Entry<Transaction, CoinInfo>) {
    CurrencyPanel(
        icon = transaction.value.image?.small,
        iconDescription = transaction.key.currencyName,
        middleColumn = {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = String.format(
                        Locale.US,
                        "%s %s",
                        if (transaction.key.currencyAmount > 0) "Bought" else "Sold",
                        transaction.value.symbol.uppercase()
                    ),
                    style = AppTypography.bodyMedium
                )

                Text(
                    text = String.format(
                        Locale.US,
                        "%s$%.2f",
                        if (transaction.key.changeAmount >= 0) "+" else "-",
                        abs(transaction.key.changeAmount)
                    ),
                    style = AppTypography.bodySmall,
                    color = defaultScheme.onSecondary
                )

                Text(
                    text = formatTimestamp(transaction.key.timestamp),
                    style = AppTypography.bodySmall,
                    color = defaultScheme.onSecondary
                )
            }
        },
        rightColumn = {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = String.format(
                        Locale.US,
                        "%+.2f  %s",
                        transaction.key.currencyAmount,
                        transaction.value.symbol.uppercase()
                    ),
                    style = AppTypography.bodyMedium,
                )
            }
        }
    )
}

fun formatTimestamp(timestamp: Long): String {
    val formatter = DateTimeFormatter.ofPattern(
        "dd MMM yyyy, h.mm a",
        Locale.ENGLISH
    )

    return Instant.ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}