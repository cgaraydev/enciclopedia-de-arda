package com.cgaraydev.enciclopediadearda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.cgaraydev.enciclopediadearda.app.navigation.ArdaNavigation
import com.cgaraydev.enciclopediadearda.ui.theme.EnciclopediaDeArdaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            EnciclopediaDeArdaTheme {
                ArdaNavigation(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}