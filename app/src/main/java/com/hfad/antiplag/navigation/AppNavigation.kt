package com.hfad.antiplag.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hfad.antiplag.presentation.about.AboutScreen
import com.hfad.antiplag.presentation.historyScreen.HistoryScreen
import com.hfad.antiplag.presentation.homeScreen.HomeScreen
import com.hfad.antiplag.presentation.logIn.LogInScreen
import com.hfad.antiplag.presentation.signUp.SignUpScreen
import com.hfad.antiplag.presentation.splashScreen.SplashScreen

//Navigation
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASHSCREEN

        //SplashRoute

    ) {
//        composable <SplashRoute>{
//            SplashScreen(navController)
//        }
//        composable <HomeRoute>{
//            HomeScreen(navController)
//        }
//        composable <AboutRoute>{
//            AboutScreen(navController)
//        }
//        composable <SplashRoute>{
//            SplashScreen(navController)
//        }
//        composable <SplashRoute>{
//            SplashScreen(navController)
//        }
//        composable <SplashRoute>{
//            SplashScreen(navController)
//        }
//        composable <SplashRoute>{
//            SplashScreen(navController)
//        }
//
        composable(Routes.SPLASHSCREEN) {
            SplashScreen(navController)

        }
        composable(Routes.HOME) {
            HomeScreen(navController)

        }
        composable(Routes.LOGIN) {
            LogInScreen(navController)
        }
        composable(Routes.SIGNUP) {
            SignUpScreen(navController)
        }
        composable(Routes.ABOUT) {
            AboutScreen(navController)

        }
        composable(Routes.HISTORY) {
            HistoryScreen(navController)

        }
        composable(Routes.RESULTS) {

        }
    }
}