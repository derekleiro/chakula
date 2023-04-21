package com.example.chakula.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.view.WindowCompat
import com.example.chakula.ChakulaApplication

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val appContainer = (application as ChakulaApplication).container
        setContent {
            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
            ChakulaApp(appContainer, widthSizeClass)
        }
    }
}
