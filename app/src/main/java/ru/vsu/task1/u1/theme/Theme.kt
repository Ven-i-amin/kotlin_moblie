package ru.vsu.task1.u1.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

val defaultScheme = darkColorScheme(
    primary = AppColors.PrimaryPurple,
    onPrimary = AppColors.LightGrey,

    secondary = AppColors.DarkGrey,
    onSecondary = AppColors.StormGrey,

    secondaryContainer = AppColors.StormGrey,

    background = AppColors.Black,
    onBackground = AppColors.LightGrey,

    surface = AppColors.Black,
    onSurface = AppColors.StormGrey,

    error = Color.Red,
    onError = AppColors.LightGrey
)