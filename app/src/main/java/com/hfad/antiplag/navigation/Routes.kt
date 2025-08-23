package com.hfad.antiplag.navigation

import android.R.id.home
import com.hfad.antiplag.navigation.Routes.ABOUT
import com.hfad.antiplag.navigation.Routes.HISTORY
import com.hfad.antiplag.navigation.Routes.HOME
import com.hfad.antiplag.navigation.Routes.RESULTS
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

object Routes {
    const val HOME = "home"
    const val RESULTS = "results"
    const val HISTORY = "history"
    const val ABOUT = "about"
    const val SPLASHSCREEN = "splashScreen"
    const val LOGIN = "logIn"
    const val SIGNUP = "signUp"
    const val LOGOUT = "logOut"
    const val DELETE = "delete"
    const val NOCONNECTION = "noConnection"
}

fun getTitle(route: String): String {
    return when(route){
        HOME -> "AntiPlag"
        RESULTS -> "AntiPlag"
        HISTORY -> "History"
        ABOUT -> "About"
        else -> ""

    }
}

//@Serializable
//object HomeRoute
//
//@Serializable
//object ResultRoute
//
//@Serializable
//object HistoryRoute
//
//@Serializable
//object AboutRoute
//
//@Serializable
//object SplashRoute
//
//@Serializable
//object LogInRoute
//
//@Serializable
//object SignUpRoute
