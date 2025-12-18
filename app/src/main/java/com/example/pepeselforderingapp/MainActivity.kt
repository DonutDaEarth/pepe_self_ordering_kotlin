package com.example.pepeselforderingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pepeselforderingapp.navigation.AppNavigation
import com.example.pepeselforderingapp.ui.theme.PepeSelfOrderingAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PepeSelfOrderingAppTheme {
                AppNavigation()
            }
        }
    }
}
    