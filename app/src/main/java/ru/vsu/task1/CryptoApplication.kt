package ru.vsu.task1

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vsu.task1.screen.trade.TradeScreen

class CryptoApplication : Application() {
    companion object {
        lateinit var instance: CryptoApplication
    }

    init {
        instance = this
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Column(Modifier.padding(10.dp)) {
                    TradeScreen()
                }
            }
        }
    }
}