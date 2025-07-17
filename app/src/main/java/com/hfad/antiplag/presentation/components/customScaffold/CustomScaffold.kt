package com.hfad.antiplag.presentation.components.customScaffold

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomScaffold(
    title: String,
    actions: @Composable () -> Unit = {},
    navigationIcon: @Composable () -> Unit = {},
    content: @Composable (innerPadding: androidx.compose.foundation.layout.PaddingValues) -> Unit,

    ) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = title,
                        color = Color.Black,
                        fontSize = 17.sp
                    )
                },
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                navigationIcon = { navigationIcon() },
                actions = { actions() },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )

            )
        }
    ) { innerPadding ->
        content(innerPadding)
    }


}