package com.hfad.antiplag.presentation.historyScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hfad.antiplag.R
import com.hfad.antiplag.navigation.Routes
import com.hfad.antiplag.presentation.components.customScaffold.CustomScaffold
import kotlinx.coroutines.launch

@Composable
fun HistoryScreen(navController: NavController) {
    CustomScaffold(
        title = stringResource(R.string.history),
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
            Image(
                painter = painterResource(id = R.drawable.navigate_before),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        //  navController.navigate(HomeRoute)
                        navController.navigate(Routes.HOME)
                    }


            )
        }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(scaffoldPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
        }
    }
}