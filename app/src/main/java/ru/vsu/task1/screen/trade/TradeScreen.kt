package ru.vsu.task1.screen.trade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.vsu.task1.R
import ru.vsu.task1.api.viewmodels.CoinGekkoViewModel
import ru.vsu.task1.composables.generic.RadioButtonButton
import ru.vsu.task1.composables.generic.RadioButtonRow
import ru.vsu.task1.composables.trade.CurrencyChart
import ru.vsu.task1.u1.theme.AppTypography
import ru.vsu.task1.u1.theme.defaultScheme

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun TradeScreen(
    currency: String = "bitcoin",
    viewModel: CoinGekkoViewModel = CoinGekkoViewModel()
) {
    val coinInfo by viewModel.coinInfo
    val prices by viewModel.prices
    val isLoading by viewModel.isLoading
    val error by viewModel.error

    var chartDays by remember { mutableIntStateOf(1) }
    var bell by remember { mutableIntStateOf(0) }
    var star by remember { mutableIntStateOf(0) }

    LaunchedEffect(coinInfo) {
        viewModel.fetchCoinInfo(currency)
    }


    LaunchedEffect(chartDays) {
        viewModel.fetchMarketChart(
            currency,
            "usdt",
            chartDays.toString()
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
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(coinInfo?.image?.large)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = currency,
                                placeholder = painterResource(id = R.drawable.ic_bitcoin_24),
                                contentScale = ContentScale.Fit
                            )
                            Text(
                                color = defaultScheme.onPrimary,
                                text = coinInfo?.name ?: "Not enabled",
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
                        IconButton(onClick = { bell = (bell + 1) % 2 }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_bell_24),
                                contentDescription = "Settings",
                                tint = defaultScheme.onSurface
                            )
                        }

                        IconButton(onClick = { star = (star + 1) % 2 }) {
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
                        @Composable
                        { selected: Boolean ->
                            RadioButtonButton(
                                modifier = Modifier
                                    .padding(bottom = 24.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (selected) defaultScheme.primary
                                        else defaultScheme.surface
                                    ),
                                selected = selected,
                                onClick = { /*TODO*/ }
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(
                                            horizontal = 17.dp,
                                            vertical = 12.dp
                                        ),
                                    text = it,
                                    style = AppTypography.bodySmall
                                )
                            }
                        }
                    }.also { it ->
                        RadioButtonRow(
                            modifier = Modifier
                                .padding(bottom = 24.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            onClick = { /*TODO*/ },
                            contents = it
                        )

                        CurrencyChart(
                            modifier = Modifier.weight(1f),
                            costs = prices.ifEmpty { listOf(0.3f, 0.2f, 12f, 10f) }
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            mapOf(
                                Pair("1D", 1),
                                Pair("1W", 7),
                                Pair("1M", 30),
                                Pair("1Y", 365)
                            ).map { pair ->
                                @Composable
                                { selected: Boolean ->
                                    RadioButtonButton(
                                        modifier = Modifier
                                            .padding(horizontal = 5.dp)
                                            .clip(shape = CircleShape)
                                            .background(
                                                if (selected) defaultScheme.primary
                                                else defaultScheme.surface
                                            ),
                                        selected = selected,
                                        onClick = { chartDays = pair.value }
                                    ) {
                                        Text(
                                            modifier = Modifier.padding(8.dp),
                                            text = pair.key,
                                            style = AppTypography.bodySmall
                                        )
                                    }
                                }
                            }.also {
                                RadioButtonRow(
                                    modifier = Modifier
                                        .padding(bottom = 24.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    onClick = { /*TODO*/ },
                                    contents = it
                                )
                            }
                        }

                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
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