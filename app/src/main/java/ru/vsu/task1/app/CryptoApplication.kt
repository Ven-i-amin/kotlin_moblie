package ru.vsu.task1.app

import android.app.Application
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.rememberNavController
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.compose.KoinContext
import org.koin.core.context.GlobalContext.startKoin
import ru.vsu.task1.app.di.appModule
import ru.vsu.task1.app.di.dataModule
import ru.vsu.task1.app.di.domainModule
import ru.vsu.task1.app.di.networkModule
import ru.vsu.task1.ui.navigation.MainNavigation
import ru.vsu.task1.ui.theme.defaultScheme

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

                    MainNavigation(navController = navController)
                }
            }
        }
    }
}