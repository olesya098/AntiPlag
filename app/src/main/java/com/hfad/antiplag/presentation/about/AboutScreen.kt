package com.hfad.antiplag.presentation.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.hfad.antiplag.R
import com.hfad.antiplag.navigation.HomeRoute
import com.hfad.antiplag.presentation.components.aboutInformation.AboutInformation
import com.hfad.antiplag.presentation.components.customScaffold.CustomScaffold

@Composable
fun AboutScreen(navController: NavHostController) {
    CustomScaffold(
        title = stringResource(R.string.about),
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.background
                )
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "AntiPlag",
                modifier = Modifier.padding(bottom = 10.dp),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 32.sp
            )
            AboutInformation(
                image = R.drawable.rocket,
                title = stringResource(R.string.speed),
                text = "instant text analysis for borrowings."
                //мгновенный анализ текста на заимствования.
            )
            AboutInformation(
                image = R.drawable.target,
                title = "Accuracy",
                // Точность
                text = "comparison with millions of sources, including academic papers and web pages."
                //  сравнение с миллионами источников, включая академические работы и веб-страницы.
            )
            AboutInformation(
                image = R.drawable.telephone,
                title = "Ease of use",
                //  Простота использования
                text = "upload a text or document in one click."
                //загружай текст или документ в один клик.
            )
            AboutInformation(
                image = R.drawable.people,
                title = "For everyone",
                //  Для всех
                text = "students, teachers, copywriters, lawyers."
                // студенты, преподаватели, копирайтеры, юристы.
            )

        }

    }

}