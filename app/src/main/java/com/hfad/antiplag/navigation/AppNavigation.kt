package com.hfad.antiplag.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hfad.antiplag.presentation.about.AboutScreen
import com.hfad.antiplag.presentation.homeScreen.HomeScreen
import com.hfad.antiplag.presentation.splashScreen.SplashScreen

//Navigation
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = SplashRoute
//        startDestination = Routes.SPLASHSCREEN
    ) {
        composable <SplashRoute>{
            SplashScreen(navController)
        }
        composable <HomeRoute>{
            HomeScreen(navController)
        }
        composable <AboutRoute>{
            AboutScreen(navController)
        }
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
//        composable(Routes.SPLASHSCREEN) {
//            SplashScreen(navController)
//
//        }
//        composable(Routes.HOME) {
//            HomeScreen(navController)
//
//        }
//        composable(Routes.LOGIN) {
//
//        }
//        composable(Routes.SIGNUP) {
//
//        }
//        composable(Routes.ABOUT) {
//            AboutScreen(navController)
//
//        }
//        composable(Routes.HISTORY) {
//
//        }
//        composable(Routes.RESULTS) {
//
//        }
    }
}