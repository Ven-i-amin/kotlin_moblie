package ru.vsu.task1.ui.screens.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import org.koin.compose.koinInject
import ru.vsu.task1.ui.composables.generic.topbar.DefaultTopBar
import ru.vsu.task1.ui.navigation.AppBarViewModel

@Composable
fun ProfileScreen(
    appBarViewModel: AppBarViewModel = koinInject(),
    viewModel: ProfileViewModel = koinInject(),
    navController: NavController
){
    LaunchedEffect(Unit) {
        appBarViewModel.setTopBar {
            DefaultTopBar(
                modifier = Modifier,
                title = "Profile",
                onClickOnHamburger = {}
            )
        }
        appBarViewModel.showBottomBar()
    }
}