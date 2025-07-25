package com.hfad.antiplag.presentation.homeScreen

import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.hfad.antiplag.navigation.Routes
import com.hfad.antiplag.presentation.components.bottonBar.BottomBar
import com.hfad.antiplag.presentation.components.customScaffold.CustomScaffold
import com.hfad.antiplag.presentation.components.navigationDrawer.NavigationDrawer
import com.hfad.antiplag.ui.theme.AntiPlagTheme
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavHostController) {
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
        ) { innerPadding ->
            CustomScaffold(
                title = "AntiPlag",
                actions = {
                    Image(
                        painter = painterResource(id = R.drawable.information),
                        contentDescription = "About",
                        modifier = Modifier
                            .size(27.dp)
                            .clickable {
                                navController.navigate(Routes.ABOUT)
                                //   navController.navigate(AboutRoute)
                            }
                    )
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
                }
            ) { scaffoldPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(scaffoldPadding)
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.weight(1f))

                    Image(
                        painter = painterResource(id = R.drawable.icon),
                        contentDescription = null,
                        modifier = Modifier.size(86.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    BottomBar()

                }
            }
        }
    }
}

