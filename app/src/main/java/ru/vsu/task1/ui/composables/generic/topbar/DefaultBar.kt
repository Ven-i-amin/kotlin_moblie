package ru.vsu.task1.ui.composables.generic.topbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import ru.vsu.task1.R
import ru.vsu.task1.ui.theme.AppTypography
import ru.vsu.task1.ui.theme.defaultScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopBar(
    modifier: Modifier = Modifier,
    title: String = "",
    onClickOnHamburger: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        TopAppBar(
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        color = defaultScheme.onPrimary,
                        text = title,
                        style = AppTypography.bodyLarge
                    )
                }
            },
            navigationIcon = {
                IconButton(
                    onClick = onClickOnHamburger
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_hamburger_24),
                        contentDescription = "Back"
                    )
                }
            },
            actions = {
                Box(modifier = Modifier.fillMaxWidth(0.1f))
            }
        )
    }
}