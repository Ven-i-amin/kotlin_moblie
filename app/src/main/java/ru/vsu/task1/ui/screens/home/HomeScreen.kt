package ru.vsu.task1.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import ru.vsu.task1.R
import ru.vsu.task1.data.models.coin.CoinInfo
import ru.vsu.task1.data.models.home.Transaction
import ru.vsu.task1.ui.composables.generic.CoinListView
import ru.vsu.task1.ui.composables.generic.DefaultTopBar
import ru.vsu.task1.ui.composables.generic.ErrorMessage
import ru.vsu.task1.ui.composables.generic.Loading
import ru.vsu.task1.ui.composables.generic.LoadingView
import ru.vsu.task1.ui.composables.home.CurrencyPanel
import ru.vsu.task1.ui.navigation.AppBarViewModel
import ru.vsu.task1.ui.screens.trade.TradeSide
import ru.vsu.task1.utils.formatTimestamp
import java.util.Locale
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel(),
    appBarViewModel: AppBarViewModel = koinInject()
) {
    LaunchedEffect(Unit) {
        appBarViewModel.setTopBar { DefaultTopBar(title = "Home") }
        appBarViewModel.showBottomBar()

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

    LoadingView(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        isLoading = isLoadingCoins || isLoadingTransaction,
        isError = error != null,
        onLoading = { Loading(modifier = Modifier.padding(horizontal = 170.dp)) },
        onError = {
            ErrorMessage(
                errorMessage = error ?: "none",
            ) {
                viewModel.fetchBalance()
                viewModel.fetchWatchlist()
                viewModel.fetchUserTransactions()
            }
        }
    ) {
        MainContent(
            navController = navController,
            transactions = transactions,
            coins = coins,
            balance = balance,
            viewModel = viewModel
        )
    }
}

@Composable
private fun MainContent(
    navController: NavController,
    transactions: Map<Transaction, CoinInfo>,
    coins: List<CoinInfo>,
    balance: Double?,
    viewModel: HomeViewModel
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp)
    ) {
        BalanceAndPurchaseButtons(
            navController = navController,
            balance = balance,
            viewModel = viewModel
        )

        TransactionList(
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
    navController: NavController,
    balance: Double?,
    viewModel: HomeViewModel,
    appBarViewModel: AppBarViewModel = koinViewModel()
) {
    val typography = MaterialTheme.typography

    var showTopUpDialog by remember { mutableStateOf(false) }
    var topUpInput by remember { mutableStateOf("") }

    fun normalizeAmountInput(value: String): String {
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

    if (showTopUpDialog) {
        val amount = topUpInput.replace(',', '.').toDoubleOrNull()
        val canConfirm = amount != null && amount > 0.0

        AlertDialog(
            onDismissRequest = { showTopUpDialog = false },
            title = { Text(text = "Top up balance") },
            text = {
                OutlinedTextField(
                    value = topUpInput,
                    onValueChange = { topUpInput = normalizeAmountInput(it) },
                    singleLine = true,
                    label = { Text(text = "Amount, USD") },
                    placeholder = { Text(text = "0") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (canConfirm) {
                            viewModel.topUpBalance(amount)
                            topUpInput = ""
                            showTopUpDialog = false
                        }
                    },
                    enabled = canConfirm
                ) {
                    Text(text = "Add")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        topUpInput = ""
                        showTopUpDialog = false
                    }
                ) {
                    Text(text = "Cancel")
                }
            }
        )
    }

    Column(
        modifier = Modifier.padding(bottom = 24.dp)
    ) {
        Column(
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Text(
                modifier = Modifier.padding(bottom = 12.dp),
                text = "Your balance",
                style = typography.bodyLarge
            )

            Text(
                text = "$${balance ?: "Not avaible"}",
                style = typography.displayLarge
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BalanceButton(
                modifier = Modifier.weight(1f),
                icon = R.drawable.ic_up_icon_24,
                text = "Top up",
                onClick = { showTopUpDialog = true }
            )

            BalanceButton(
                modifier = Modifier.weight(1f),
                icon = R.drawable.ic_plus_24,
                text = "Buy",
                onClick = {
                    appBarViewModel.setPressedButton("coins")
                    navController.navigate("coins?tradeSheetState=${TradeSide.Buy.value}")
                }
            )

            BalanceButton(
                modifier = Modifier.weight(1f),
                icon = R.drawable.ic_minus_24,
                text = "Sell",
                onClick = {
                    appBarViewModel.setPressedButton("coins")
                    navController.navigate("coins?tradeSheetState=${TradeSide.Sell.value}")
                }
            )
        }
    }
}

@Composable
private fun BalanceButton(
    modifier: Modifier = Modifier,
    icon: Int,
    text: String,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Button(
        modifier = modifier
            .aspectRatio(1f),
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.onSecondary.copy(0.33f)
        ),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(24.dp),
        onClick = onClick,
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
                style = typography.bodySmall
            )
        }
    }
}

@Composable
private fun TransactionList(
    transactions: Map<Transaction, CoinInfo>
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

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
                style = typography.bodyMedium
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
                    color = colors.primary,
                    style = typography.bodyMedium
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
                    style = typography.bodyMedium,
                    color = colors.onSurface
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
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Column {
        Row(
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Text(
                text = "Watchlist",
                style = typography.bodyMedium
            )
        }

        if (coins.isEmpty()) {
            return Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No coins in watchlist",
                    style = typography.bodyMedium,
                    color = colors.onSurface
                )
            }
        }

        CoinListView(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            coins = coins,
            onItemClick = { coinInfo ->
                navController.navigate("trade/${coinInfo.id}")
            }
        )
    }
}

@Composable
fun TransactionPanel(transaction: Map.Entry<Transaction, CoinInfo>) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    CurrencyPanel(
        icon = transaction.value.image,
        iconDescription = transaction.key.currencyId,
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
                    style = typography.bodyMedium
                )

                Text(
                    text = String.format(
                        Locale.US,
                        "%s$%.2f",
                        if (transaction.key.changeAmount >= 0) "+" else "-",
                        abs(transaction.key.changeAmount)
                    ),
                    style = typography.bodySmall,
                    color = colors.onSecondary
                )

                Text(
                    text = formatTimestamp(transaction.key.timestamp),
                    style = typography.bodySmall,
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
                    text = String.format(
                        Locale.US,
                        "%+.2f  %s",
                        transaction.key.currencyAmount,
                        transaction.value.symbol.uppercase()
                    ),
                    style = typography.bodyMedium,
                )
            }
        }
    )
}
