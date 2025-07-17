package com.hfad.antiplag.presentation.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.hfad.antiplag.R
import com.hfad.antiplag.navigation.Routes
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
                      //  navController.navigate(HomeRoute)
                        navController.navigate(Routes.HOME)
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
                modifier = Modifier.padding(bottom = 10.dp, top = 10.dp),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 32.sp
            )
            AboutInformation(
                image = R.drawable.rocket,
                title = stringResource(R.string.speed),
                text = stringResource(R.string.instant_text_analysis_for_borrowings)
            )
            AboutInformation(
                image = R.drawable.target,
                title = stringResource(R.string.accuracy),
                text = stringResource(R.string.comparison_with_millions_of_sources_including_academic_papers_and_web_pages)
            )
            AboutInformation(
                image = R.drawable.telephone,
                title = stringResource(R.string.ease_of_use),
                text = stringResource(R.string.upload_a_text_or_document_in_one_click)
            )
            AboutInformation(
                image = R.drawable.people,
                title = stringResource(R.string.for_everyone),
                text = stringResource(R.string.students_teachers_copywriters_lawyers)
            )

        }

    }

}