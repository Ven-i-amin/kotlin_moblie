package ru.vsu.task1.ui.screens.portfolio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import ru.vsu.task1.ui.composables.generic.CoinListView
import ru.vsu.task1.ui.composables.generic.ErrorMessage
import ru.vsu.task1.ui.composables.generic.Loading
import ru.vsu.task1.ui.composables.generic.LoadingView
import ru.vsu.task1.ui.composables.generic.topbar.DefaultTopBar
import ru.vsu.task1.ui.composables.portfolio.PieChart
import ru.vsu.task1.ui.composables.trade.ValueAndChangeColumn
import ru.vsu.task1.ui.navigation.AppBarViewModel
import kotlin.math.abs

private const val PIE_RADIUS = 140

private const val PIE_WIDTH = 30

@Composable
fun PortfolioScreen(
    navController: NavController,
    viewModel: PortfolioViewModel = koinViewModel(),
    appBarViewModel: AppBarViewModel = koinInject()
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    val userCoinsAndInfo by viewModel.userCoinsAndInfo.collectAsState()
    val coinsBalances by viewModel.coinsBalances.collectAsState()

    var selectedCoin by remember { mutableIntStateOf(-1) }

    LaunchedEffect(Unit) {
        appBarViewModel.setTopBar {
            DefaultTopBar(
                modifier = Modifier,
                title = "Porfolio",
                onClickOnHamburger = {}
            )
        }
        appBarViewModel.showBottomBar()

        viewModel.getUserCoins()
    }

    LoadingView(
        isLoading = loading,
        isError = error != null,
        onLoading = { Loading() },
        onError = { ErrorMessage { viewModel.getUserCoins() } }
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            ValueAndChangeColumn(
                modifier = Modifier.padding(vertical = 24.dp),
                value = 1000.0,
                change = 100.0,
                percentageChange = 10.0,
                overall = "Overall"
            )

            Box(modifier = Modifier.padding(bottom = 24.dp)) {
                PieChart(
                    data = coinsBalances.map { it.first to abs(it.second) },
                    pieRadius = PIE_RADIUS.dp,
                    pieLineWidth = PIE_WIDTH.dp
                ) { coin ->
                    selectedCoin = coin
                }


                if (selectedCoin != -1) {
                    Column(
                        modifier = Modifier.height((PIE_RADIUS * 2).dp).fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            modifier = Modifier.padding(bottom = 6.dp),
                            text = coinsBalances[selectedCoin].first + " balance",
                            style = typography.titleLarge,
                        )

                        val value = coinsBalances[selectedCoin].second

                        if (value <= 0) {
                            ValueAndChangeColumn(
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                value = null,
                                change = null,
                                percentageChange = null,
                                overall = ""
                            )
                        } else {
                            val coinInfo = userCoinsAndInfo[selectedCoin].second

                            ValueAndChangeColumn(
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                value = value,
                                change = coinInfo?.priceChange24h,
                                percentageChange = coinInfo?.priceChangePercentage24h,
                                overall = ""
                            )
                        }

                    }
                }
            }

            CoinListView(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                coins = userCoinsAndInfo.mapNotNull { it.second },
                navController = navController
            )
        }
    }
}