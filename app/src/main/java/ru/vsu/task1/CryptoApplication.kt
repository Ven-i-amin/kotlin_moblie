package ru.vsu.task1

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.vsu.task1.screen.trade.TradeScreen

class CryptoApplication : Application() {
    companion object {
        lateinit var instance: CryptoApplication
    }

    override fun onCreate() {
        super.onCreate()

    }

    init {
        instance = this
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TradeScreen()
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun mainView() {
    MaterialTheme {
        Column(Modifier.padding(10.dp)) {
            Text(text = "Hello")
            Text(text = "World")
        }
    }
}