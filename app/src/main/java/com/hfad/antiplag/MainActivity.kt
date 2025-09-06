package com.hfad.antiplag

import android.content.pm.ActivityInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hfad.antiplag.navigation.AppNavigation
import com.hfad.antiplag.ui.theme.AntiPlagTheme
import com.hfad.antiplag.viewModel.ThemeViewModel
import java.security.MessageDigest


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = viewModel()
            themeViewModel.initialize(this)
            
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
            
            AntiPlagTheme(darkTheme = isDarkTheme) {
                AppNavigation(themeViewModel = themeViewModel)
            }
        }
    }
}
