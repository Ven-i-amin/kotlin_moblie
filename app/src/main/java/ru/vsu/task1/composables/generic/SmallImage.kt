package ru.vsu.task1.composables.generic

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.vsu.task1.R

@Composable
fun SmallImage(modifier: Modifier = Modifier, url: String?, description: String) {
    AsyncImage(
        modifier = modifier.height(32.dp),
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentDescription = description,
        placeholder = painterResource(id = R.drawable.ic_bitcoin_24),
        contentScale = ContentScale.Fit
    )
}