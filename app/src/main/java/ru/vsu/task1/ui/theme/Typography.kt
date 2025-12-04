package ru.vsu.task1.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.vsu.task1.R

val ClashGrotesk = FontFamily(
    Font(R.font.clash_grotesk_variable, FontWeight.Bold)
)

val NunitoSans = FontFamily(Font(R.font.nunito_sans))

val AppTypography = Typography(

    // Заголовки
    displayLarge = TextStyle(
        fontFamily = ClashGrotesk,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,
        lineHeight = (34 * 1.5).sp, // 150%
        letterSpacing = 0.sp
    ),

    // Основной текст (Body1)
    bodyLarge = TextStyle(
        fontFamily = NunitoSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = (22 * 1.5).sp, // 150%
        letterSpacing = 0.sp
    ),

    // Body2
    bodyMedium = TextStyle(
        fontFamily = NunitoSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        lineHeight = (17 * 1.5).sp, // 150%
        letterSpacing = 0.sp
    ),

    // Body3
    bodySmall = TextStyle(
        fontFamily = NunitoSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = (15 * 1.5).sp, // 150%
        letterSpacing = 0.sp
    ),

    // Caption1
    labelSmall = TextStyle(
        fontFamily = NunitoSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = (12 * 1.5).sp, // 150%
        letterSpacing = 0.sp
    )
)