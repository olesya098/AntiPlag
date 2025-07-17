package com.hfad.antiplag.presentation.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hfad.antiplag.R
import com.hfad.antiplag.navigation.HomeRoute
import com.hfad.antiplag.presentation.components.customScaffold.CustomScaffold

@Composable
fun AboutScreen(navController: NavHostController) {
    CustomScaffold(
        title = "AntiPlag",
        actions = {


        },
        navigationIcon = {

            Image(
                painter = painterResource(id = R.drawable.navigate_before),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        navController.navigate(HomeRoute)
//                        navController.navigate(Routes.HOME)
                    }


            )

        },
    ) { paddingValues ->

    }

}