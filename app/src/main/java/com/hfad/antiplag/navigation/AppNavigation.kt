package com.hfad.antiplag.navigation

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hfad.antiplag.data.network.NetworkFun
import com.hfad.antiplag.presentation.about.AboutScreen
import com.hfad.antiplag.presentation.historyScreen.HistoryScreen
import com.hfad.antiplag.presentation.homeScreen.HomeScreen
import com.hfad.antiplag.presentation.logIn.LogInScreen
import com.hfad.antiplag.presentation.noConnection.NoConnection
import com.hfad.antiplag.presentation.result.ResultScreen
import com.hfad.antiplag.presentation.signUp.SignUpScreen
import com.hfad.antiplag.presentation.splashScreen.SplashScreen
import com.hfad.antiplag.viewModel.LoginSigninViewModel
import com.hfad.antiplag.viewModel.PlagiarismCheckViewModel

//Navigation
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val plagiarismCheckViewModel = viewModel<PlagiarismCheckViewModel>()
    val logInSignInViewModel = viewModel<LoginSigninViewModel>()

    val context = LocalContext.current

    // Последний реальный экран приложения (не включая экран отсутствия сети)
    val lastNonNoConnectionRouteState = remember { mutableStateOf(Routes.SPLASHSCREEN) }

    // Отслеживаем смену экранов, чтобы запоминать, где был пользователь
    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            val route = destination.route
            if (route != null && route != Routes.NOCONNECTION) {
                lastNonNoConnectionRouteState.value = route
            }
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose { navController.removeOnDestinationChangedListener(listener) }
    }

    // Постоянное наблюдение за подключением к интернету
    DisposableEffect(context) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        fun isOnline(): Boolean = NetworkFun.isInternetAvailable(context)

        fun navigateToNoConnectionIfNeeded() {//Проверяет, если мы еще не на экране "Нет соединения", то переводит нас на него.
            val currentRoute = navController.currentDestination?.route
            if (currentRoute != Routes.NOCONNECTION) {
                navController.navigate(Routes.NOCONNECTION) {
                    launchSingleTop = true
                }
            }
        }

        fun navigateBackToLastIfNeeded() {//Если мы находимся на экране "Нет соединения" и интернет появился, то возвращает нас на последний запомненный "настоящий" экран
            val currentRoute = navController.currentDestination?.route
            if (currentRoute == Routes.NOCONNECTION) {
                val target = lastNonNoConnectionRouteState.value
                navController.navigate(target) {
                    popUpTo(Routes.NOCONNECTION) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {// Вызывается, когда система обнаружила любую новую сеть
                ContextCompat.getMainExecutor(context).execute {
                    navigateBackToLastIfNeeded()
                }
            }

            override fun onLost(network: Network) {//Вызывается, когда пропадает конкретное сетевое подключение
                ContextCompat.getMainExecutor(context).execute {
                    navigateToNoConnectionIfNeeded()
                }
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                // Вызывается, когда у сети появляется больше информации о себе. Здесь мы проверяем, есть ли у этой сети на самом деле интернет
                // (проверяя TRANSPORT_WIFI, CELLULAR, ETHERNET).
                // В зависимости от результата, либо возвращаем пользователя в приложение,
                // либо отправляем на экран "Нет сети".
                val hasInternet =
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                ContextCompat.getMainExecutor(context).execute {
                    if (hasInternet) navigateBackToLastIfNeeded() else navigateToNoConnectionIfNeeded()
                }
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, networkCallback)

        // Первичная проверка при запуске композиции
        if (!isOnline()) {
            navigateToNoConnectionIfNeeded()
        }

        onDispose { connectivityManager.unregisterNetworkCallback(networkCallback) }
    }

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
            HomeScreen(navController, plagiarismCheckViewModel)

        }
        composable(Routes.LOGIN) {
            LogInScreen(navController, logInSignInViewModel)
        }
        composable(Routes.SIGNUP) {
            SignUpScreen(navController, logInSignInViewModel)
        }
        composable(Routes.ABOUT) {
            AboutScreen(navController)

        }
        composable(Routes.HISTORY) {
            HistoryScreen(navController)

        }
        composable(Routes.RESULTS) {
            ResultScreen(navController,plagiarismCheckViewModel)
        }
        composable(Routes.NOCONNECTION) {
            NoConnection(navController)
        }
    }
}