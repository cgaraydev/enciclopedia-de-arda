package com.cgaraydev.enciclopediadearda.feature.calendar.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun TodayInArdaLifecycleEffect(
    onRefresh: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val refresh by rememberUpdatedState(onRefresh)

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            while (isActive) {
                refresh()
                delay(60_000L)
            }
        }
    }
}