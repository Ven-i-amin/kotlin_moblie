package ru.vsu.task1.ui.composables.generic.topbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import ru.vsu.task1.R
import ru.vsu.task1.domain.models.coin.CoinInfo
import ru.vsu.task1.ui.composables.generic.SmallImage
import ru.vsu.task1.ui.theme.AppTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradeTopBar(
    modifier: Modifier,
    navController: NavController,
    currency: String = "bitcoin",
    coinInfo: CoinInfo?,
) {
    val colors = MaterialTheme.colorScheme
    var bell by remember { mutableIntStateOf(0) }
    var star by remember { mutableIntStateOf(0) }

    Column(modifier = modifier) {
        TopAppBar(
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (coinInfo?.image != null) {
                        SmallImage(
                            url = coinInfo.image,
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