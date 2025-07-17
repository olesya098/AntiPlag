package com.hfad.antiplag.presentation.homeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hfad.antiplag.R
import com.hfad.antiplag.navigation.AboutRoute
import com.hfad.antiplag.presentation.components.bottonBar.BottomBar
import com.hfad.antiplag.presentation.components.customScaffold.CustomScaffold

@Composable
fun HomeScreen(navController: NavHostController) {
    CustomScaffold(
        title = "AntiPlag",
        actions = {
            Image(
                painter = painterResource(id = R.drawable.information),
                contentDescription = null,
                modifier = Modifier
                    .size(27.dp)
                    .clickable {
                        navController.navigate(AboutRoute)
//                        navController.navigate(Routes.ABOUT)
                    }
            )

        },
        navigationIcon = {

            Image(
                painter = painterResource(id = R.drawable.menu),
                contentDescription = null,
                modifier = Modifier.size(24.dp)

            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.White
                )
                .padding(paddingValues),
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