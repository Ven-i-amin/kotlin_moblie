package ru.vsu.task1.ui.screens.coins

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import ru.vsu.task1.ui.composables.coins.TopCurrencyPanel
import ru.vsu.task1.ui.composables.generic.CoinListView
import ru.vsu.task1.ui.composables.generic.ErrorMessage
import ru.vsu.task1.ui.composables.generic.Loading
import ru.vsu.task1.ui.composables.generic.LoadingView
import ru.vsu.task1.ui.composables.generic.DefaultTopBar
import ru.vsu.task1.ui.composables.generic.loadContentWhenListEnding
import ru.vsu.task1.ui.composables.generic.simpleVerticalScrollbar
import ru.vsu.task1.ui.navigation.AppBarViewModel

@Composable
fun CoinsScreen(
    tradeSheetState: String?,
    navController: NavController,
    viewModel: CoinsViewModel = koinViewModel(),
    appBarViewModel: AppBarViewModel = koinInject(),
) {
    LaunchedEffect(Unit) {
        appBarViewModel.setTopBar { DefaultTopBar(title = "Coins")}
        appBarViewModel.showBottomBar()

        viewModel.fetchCoins()
    }

    val isLoading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val isTopLoading by viewModel.loadingTop.collectAsState()
    val errorTop by viewModel.errorTop.collectAsState()

    val coinsSubList by viewModel.coinsSubList.collectAsState()
    val topPercentage24h by viewModel.topPercentageChange24h.collectAsState()

    val scrollState = rememberScrollState()

    LoadingView(
        isLoading = isLoading,
        isError = error != null,
        onLoading = { Loading() },
        onError = { ErrorMessage { viewModel.fetchCoins() } }
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .simpleVerticalScrollbar(
                        width = 2.dp,
                        state = scrollState
                    )
                    .loadContentWhenListEnding(
                        state = scrollState,
                        onLoad = {
                            viewModel.addSubList()
                        }
                    )
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "Top coins for 24 hours",
                    style = MaterialTheme.typography.titleLarge,
                )

                LoadingView(
                    isLoading = isTopLoading,
                    isError = errorTop != null,
                    onLoading = { Loading() },
                    onError = { ErrorMessage { errorTop } }
                ) {
                    Row(
                        modifier = Modifier.padding(bottom = 12.dp).weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        topPercentage24h.forEach {
                            TopCurrencyPanel(
                                modifier = Modifier.weight(1f).height(150.dp),
                                coinInfo = it.key,
                                coinChart = it.value,
                                onClick = {
                                    navController.navigate("trade/${it.key.id}?tradeSheetState=$tradeSheetState")
                                }
                            )
                        }
                    }
                }

                Text(
                    text = "All coins",
                    style = MaterialTheme.typography.titleLarge
                )


                CoinListView(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    coins = coinsSubList,
                    onItemClick = { coinInfo ->
                        navController.navigate("trade/${coinInfo.id}?tradeSheetState=$tradeSheetState")
                    }
                )
            }
        }
    }
}