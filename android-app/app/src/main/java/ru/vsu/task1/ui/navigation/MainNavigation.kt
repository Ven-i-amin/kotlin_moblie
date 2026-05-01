package ru.vsu.task1.ui.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.koin.compose.koinInject
import ru.vsu.task1.R
import ru.vsu.task1.ui.screens.auth.AuthScreen
import ru.vsu.task1.ui.screens.coins.CoinsScreen
import ru.vsu.task1.ui.screens.home.HomeScreen
import ru.vsu.task1.ui.screens.portfolio.PortfolioScreen
import ru.vsu.task1.ui.screens.profile.ProfileScreen
import ru.vsu.task1.ui.screens.trade.TradeScreen

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    appBarViewModel: AppBarViewModel = koinInject()
) {
    val colors = MaterialTheme.colorScheme

    val topBar by appBarViewModel.topBar.collectAsState()
    val bottomBarVisible by appBarViewModel.bottomBarVisible.collectAsState()
    val pressedButton by appBarViewModel.pressedButton.collectAsState()

    Log.d("navigation view", bottomBarVisible.toString())

    Scaffold(
        topBar = topBar,
        bottomBar = {
            if (!bottomBarVisible) {
                return@Scaffold
            }

            NavigationBar(
                containerColor = colors.background,

                ) {
                val icons = listOf(
                    R.drawable.ic_home_24 to "home",
                    R.drawable.ic_portfolio_24 to "portfolio",
                    R.drawable.ic_trade_24 to "coins",
                    R.drawable.ic_account_24 to "profile"
                )

                icons.forEach { iconAndEndPoint ->
                    NavigationBarItem(
                        selected = pressedButton == iconAndEndPoint.second,
                        onClick = {
                            appBarViewModel.setPressedButton(iconAndEndPoint.second)
                            navController.navigate(iconAndEndPoint.second)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = colors.primary,
                            indicatorColor = Color.Transparent,
                            unselectedIconColor = colors.onSurface
                        ),
                        icon = {
                            Icon(
                                painter = painterResource(id = iconAndEndPoint.first),
                                contentDescription = "Home"
                            )
                        }
                    )
                }
            }
        }
    ) { pd ->
        Box(modifier = Modifier.padding(pd)) {
            NavHost(
                modifier = modifier,
                navController = navController,
                startDestination = "auth"
            ) {
                composable("auth") {
                    AuthScreen(navController)
                }

                composable("home") {
                    HomeScreen(navController)
                }

                composable(
                    route = "coins?tradeSheetState={tradeSheetState}",
                    arguments = listOf(
                        navArgument("tradeSheetState") {
                            type = NavType.StringType
                            nullable = true
                            defaultValue = null
                        }
                    )
                ) {
                    val tradeSheetState = it.arguments?.getString("tradeSheetState")

                    CoinsScreen(
                        tradeSheetState = tradeSheetState,
                        navController = navController
                    )
                }

                composable("portfolio") {
                    PortfolioScreen(navController)
                }

                composable("profile") {
                    ProfileScreen(navController = navController)
                }

                composable(
                    route = "trade/{coin}?tradeSheetState={tradeSheetState}",
                    arguments = listOf(
                        navArgument("coin") { type = NavType.StringType },
                        navArgument("tradeSheetState") {
                            type = NavType.StringType
                            nullable = true
                            defaultValue = null
                        }
                    )
                ) { backStackEntry ->
                    val coin = backStackEntry.arguments?.getString("coin")!!
                    val tradeSheetState = backStackEntry.arguments?.getString("tradeSheetState")

                    TradeScreen(navController, coin, tradeSheetState)
                }
            }
        }
    }
}

