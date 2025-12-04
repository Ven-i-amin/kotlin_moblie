package ru.vsu.task1

import android.app.Application
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.compose.KoinContext
import org.koin.core.context.GlobalContext.startKoin
import ru.vsu.task1.di.appModule
import ru.vsu.task1.di.dataModule
import ru.vsu.task1.di.domainModule
import ru.vsu.task1.di.networkModule
import ru.vsu.task1.screen.auth.AuthScreen
import ru.vsu.task1.screen.home.HomeScreen
import ru.vsu.task1.screen.trade.TradeScreen
import ru.vsu.task1.u1.theme.defaultScheme

class CryptoApplication : Application() {
    companion object {
        lateinit var instance: CryptoApplication
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@CryptoApplication)
            modules(appModule, dataModule, domainModule, networkModule)
        }
    }

    init {
        instance = this
    }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KoinContext {
                MaterialTheme(defaultScheme) {
                    val navController = rememberNavController()

                    NavHost(
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
            }
        }
    }
}