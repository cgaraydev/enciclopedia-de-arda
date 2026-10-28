package com.cgaraydev.enciclopediadearda.ui.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val ArdaColorScheme = darkColorScheme(
    primary = TolkienGold,
    onPrimary = TolkienBlack,
    background = TolkienBlack,
    onBackground = TolkienWhite,
    surface = TolkienBlack,
    onSurface = TolkienWhite,
    error = TolkienRed,
    onError = TolkienWhite
)

@Composable
fun EnciclopediaDeArdaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ArdaColorScheme,
        typography = TolkienTypography
    ) {
        CompositionLocalProvider(
            LocalIndication provides ripple(color = TolkienGold)
        ) {
            content()
        }
    }
}