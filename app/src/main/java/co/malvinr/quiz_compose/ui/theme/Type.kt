package co.malvinr.quiz_compose.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import co.malvinr.quiz_compose.R

private val fontFamilyPoppins = FontFamily(
    listOf(
        Font(
            resId = R.font.poppins_regular
        ),
        Font(
            resId = R.font.poppins_semibold,
            weight = FontWeight.SemiBold
        ),
        Font(
            resId = R.font.poppins_bold,
            weight = FontWeight.Bold
        )
    )
)

val typography = Typography(
    displayLarge = TextStyle(
        fontFamily = fontFamilyPoppins,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = fontFamilyPoppins,
        fontSize = 45.sp,
        lineHeight = 52.sp
    ),
    displaySmall = TextStyle(
        fontFamily = fontFamilyPoppins,
        fontSize = 36.sp,
        lineHeight = 44.sp
    ),
    titleMedium = TextStyle(
        fontFamily = fontFamilyPoppins,
        fontWeight = FontWeight(500),
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = (0.15).sp
    ),
    bodySmall = TextStyle(
        fontFamily = fontFamilyPoppins,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = (0.4).sp
    ),
    bodyMedium = TextStyle(
        fontFamily = fontFamilyPoppins,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = (0.25).sp
    ),
    bodyLarge = TextStyle(
        fontFamily = fontFamilyPoppins,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = (0.5).sp
    ),
    labelMedium = TextStyle(
        fontFamily = fontFamilyPoppins,
        fontWeight = FontWeight(500),
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = (0.5).sp,
        textAlign = TextAlign.Center
    ),
    labelLarge = TextStyle(
        fontFamily = fontFamilyPoppins,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = (0.1).sp
    )
)
