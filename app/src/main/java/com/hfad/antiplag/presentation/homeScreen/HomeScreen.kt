package com.hfad.antiplag.presentation.homeScreen

import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hfad.antiplag.R
import com.hfad.antiplag.model.PlagiatCheckState
import com.hfad.antiplag.navigation.Routes
import com.hfad.antiplag.presentation.components.bottonBar.BottomBar
import com.hfad.antiplag.presentation.components.message.Message
import com.hfad.antiplag.presentation.components.navigationDrawer.NavigationDrawer
import com.hfad.antiplag.ui.theme.AntiPlagTheme
import com.hfad.antiplag.viewModel.LoginSigninViewModel
import com.hfad.antiplag.viewModel.PlagiarismCheckViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    plagiarismCheckViewModel: PlagiarismCheckViewModel,
    viewModel: LoginSigninViewModel,
) {

    val email by viewModel.email.collectAsState()
    val state = plagiarismCheckViewModel.checkState.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var isDarkTheme by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            permissions.entries.forEach {
                val isGranted = it.value
                if (isGranted) {
                    Log.d("permissions", "permissions is yes")
                } else {
                    Toast.makeText(context, "permissions is noooo", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        launcher.launch(
            arrayOf(
                Manifest.permission.INTERNET,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        )
    }

    AntiPlagTheme(darkTheme = isDarkTheme) {
        NavigationDrawer(
            drawerState = drawerState,
            onMenuClick = {
                scope.launch {
                    if (drawerState.isClosed) drawerState.open()
                    else drawerState.close()
                }
            },
            navController = navController,
            isDarkTheme = isDarkTheme,
            onThemeChange = {
                isDarkTheme = !isDarkTheme
            },
            onOut = {
                viewModel.signOut()
            },
            email = email,
            onDelete = {
                viewModel.signDelete { success ->
                    if (success){
                        Log.d("DELETE ACCOUNT", "account deleted")
                    }
                }
            },
            onNavigateToLogin = {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            }
        ) { innerPadding ->
            Scaffold(
                topBar = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(innerPadding)
                    ) {
                        // TopBar content
                        androidx.compose.material3.TopAppBar(
                            title = {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    androidx.compose.material3.Text(
                                        "AntiPlag",
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            },
                            navigationIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.menu),
                                    contentDescription = "Menu",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clickable {
                                            scope.launch {
                                                if (drawerState.isClosed) drawerState.open()
                                                else drawerState.close()
                                            }
                                        }
                                )
                            },
                            actions = {
                                Image(
                                    painter = painterResource(id = R.drawable.information),
                                    contentDescription = "About",
                                    modifier = Modifier
                                        .size(27.dp)
                                        .clickable {
                                            navController.navigate(Routes.ABOUT)
                                        }
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.background
                            )
                        )
                    }
                },
                bottomBar = {
                    // BottomBar всегда прикреплен к низу
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        BottomBar(plagiarismCheckViewModel)
                    }
                },
                containerColor = MaterialTheme.colorScheme.background
            ) { scaffoldPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(scaffoldPadding)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    // Основной контент - центрированный
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        when (state.value) {
                            PlagiatCheckState.Idle -> {
                                Image(
                                    painter = painterResource(id = R.drawable.icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(86.dp)
                                )
                            }

                            PlagiatCheckState.SendText -> {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Message(
                                        text = "Текст отправлен"
                                    )
                                }
                            }

                            is PlagiatCheckState.CheckingStatus -> {
                                Message(
                                    text = "Текст обрабатывается ${(state.value as PlagiatCheckState.CheckingStatus).progress}"
                                )
                            }

                            is PlagiatCheckState.Error -> {
                                Message(
                                    text = "Error  ${(state.value as PlagiatCheckState.Error).message}"
                                )
                            }

                            is PlagiatCheckState.Success -> {
                                plagiarismCheckViewModel.updateReport(
                                    (state.value as PlagiatCheckState.Success).report
                                )
                                Message(
                                    text = "Узнать результат",
                                    modifier = Modifier.clickable {
                                        navController.navigate(Routes.RESULTS)
                                    }
                                )
                            }

                            is PlagiatCheckState.WaitingStatus -> {
                                Message(
                                    text = "..."
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}