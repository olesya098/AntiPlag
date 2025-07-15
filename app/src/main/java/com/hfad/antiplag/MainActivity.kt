package com.hfad.antiplag

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hfad.antiplag.navigation.AppNavigation
import com.hfad.antiplag.ui.theme.AntiPlagTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AntiPlagTheme {

                AppNavigation()


            }
        }
    }
}

