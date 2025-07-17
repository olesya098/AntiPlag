package com.hfad.antiplag.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hfad.antiplag.presentation.homeScreen.HomeScreen
import com.hfad.antiplag.presentation.splashScreen.SplashScreen

//Navigation
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASHSCREEN
    ) {
        composable(Routes.SPLASHSCREEN) {
            SplashScreen(navController)

        }
        composable(Routes.HOME) {
            HomeScreen()

        }
        composable(Routes.LOGIN) {

        }
        composable(Routes.SIGNUP) {

        }
        composable(Routes.ABOUT) {

        }
        composable(Routes.HISTORY) {

        }
        composable(Routes.RESULTS) {

        }
    }
}