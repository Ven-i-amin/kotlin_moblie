package ru.vsu.task1.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppBarViewModel : ViewModel() {
    private val _bottomBarVisible = MutableStateFlow(true)
    val bottomBarVisible = _bottomBarVisible.asStateFlow()

    private val _pressedButton = MutableStateFlow("home")
    val pressedButton = _pressedButton.asStateFlow()

    private val _topbar = MutableStateFlow<@Composable () -> Unit>({  })
    val topBar = _topbar.asStateFlow()

    fun setPressedButton(pressedButton: String){
        _pressedButton.value = pressedButton
    }

    fun setTopBar(newTopBar: @Composable () -> Unit) {
        _topbar.value = newTopBar
    }

    fun hideBottomBar() {
        Log.d("appBarViewModel", "bottom bar hidden")
        _bottomBarVisible.value = false
    }

    fun showBottomBar() {
        Log.d("appBarViewModel", "bottom bar showed")
        _bottomBarVisible.value = true
    }
}