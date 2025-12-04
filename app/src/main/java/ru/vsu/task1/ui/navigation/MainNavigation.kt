package ru.vsu.task1.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.vsu.task1.ui.screens.auth.AuthScreen
import ru.vsu.task1.ui.screens.home.HomeScreen
import ru.vsu.task1.ui.screens.trade.TradeScreen

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
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

        composable("trade/{coin}") { backStackEntry ->
            val coin = backStackEntry.arguments?.getString("coin")!!
            TradeScreen(navController, coin)
        }
    }
}