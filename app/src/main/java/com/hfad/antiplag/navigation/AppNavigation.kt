package com.hfad.antiplag.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation (){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASHSCREEN
    ) {
        composable(Routes.SPLASHSCREEN){

        }
        composable(Routes.HOME){

        }
        composable(Routes.LOGIN){

        }
        composable(Routes.SIGNUP){

        }
        composable(Routes.ABOUT){

        }
        composable(Routes.HISTORY){

        }
        composable(Routes.RESULTS){

        }
    }
}