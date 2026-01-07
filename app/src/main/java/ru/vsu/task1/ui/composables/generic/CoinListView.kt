package ru.vsu.task1.ui.composables.generic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import ru.vsu.task1.data.models.coin.CoinInfo
import ru.vsu.task1.ui.composables.home.CurrencyPanel
import ru.vsu.task1.ui.theme.AppTypography
import ru.vsu.task1.ui.theme.defaultScheme
import java.util.Locale
import kotlin.math.abs

@Composable
fun CoinListView(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    coins: List<CoinInfo>,
    navController: NavController
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        for (coin in coins) {
            CurrencyPanel(
                modifier = Modifier.clickable(
                    onClick = { navController.navigate("trade/${coin.id}") }
                ),
                icon = coin.image,
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
                        val priceText = coin.currentPrice?.let { price ->
                            String.format(Locale.US, "$%.2f", price)
                        } ?: "Not available"

                        Text(
                            text = priceText,
                            style = AppTypography.bodyMedium,
                        )

                        val percentageChange = coin.priceChangePercentage24h ?: 0.0

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

@Composable
fun CoinList(
    coins: List<CoinInfo>,
    navController: NavController
): List<@Composable () -> Unit> {
    return coins.map { coin ->
        @Composable {
            CurrencyPanel(
                modifier = Modifier.clickable(
                    onClick = { navController.navigate("trade/${coin.id}") }
                ),
                icon = coin.image,
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
                        val priceText = coin.currentPrice?.let { price ->
                            String.format(Locale.US, "$%.2f", price)
                        } ?: "Not available"

                        Text(
                            text = priceText,
                            style = AppTypography.bodyMedium,
                        )

                        val percentageChange = coin.priceChangePercentage24h ?: 0.0

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