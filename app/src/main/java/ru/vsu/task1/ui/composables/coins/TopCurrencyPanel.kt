package ru.vsu.task1.ui.composables.coins

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.vsu.task1.data.models.coin.CoinInfo
import ru.vsu.task1.ui.composables.trade.CurrencyChart
import ru.vsu.task1.ui.theme.defaultScheme
import ru.vsu.task1.utils.formatPercentage
import ru.vsu.task1.utils.formatPrice

@Composable
fun TopCurrencyPanel(
    coinInfo: CoinInfo?,
    coinChart: List<Float>,
    modifier: Modifier = Modifier,
    navController: NavController? = null
) {
    val colors = MaterialTheme.colorScheme
    
    Column(
        modifier = modifier
            .background(colors.secondary)
            .padding(vertical = 12.dp)
            .padding(start = 12.dp)
            .padding(end = 6.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { navController?.navigate("trade/${coinInfo?.id}") }
    ) {
        Column(
            modifier = Modifier
        ) {
            Text(
                text = coinInfo?.symbol?.uppercase() ?: "Not available",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = formatPrice(coinInfo?.currentPrice),
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = formatPercentage(coinInfo?.priceChangePercentage24h),
                style = MaterialTheme.typography.titleLarge,
                color = colors.primary
            )
        }

        CurrencyChart(
            prices = coinChart,
            shadow = false,
            marker = false,
            priceLine = false,
            modifier = Modifier
                .padding(top = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    val coinInfo = CoinInfo(
        id = "bitcoin",
        symbol = "btc",
        bitgetSymbol = "BTCUSDT",
        name = "Bitcoin",
        image = "https://assets.coingecko.com/coins/images/1/large/bitcoin.png?1547",
        currentPrice = 25000.0,
        priceChange24h = 10.0,
        priceChangePercentage24h = 100.0,
    )

    MaterialTheme(colorScheme = defaultScheme) {
        TopCurrencyPanel(coinInfo, listOf(8f, 2f, 1f, 4f, 10f))
    }
}
