package com.hfad.antiplag.presentation.components.aboutInformation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.hfad.antiplag.R

@Composable
fun AboutInformation(
    image: Int,
    title: String,
    text: String
    ) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(10.dp),

    ) {
        Image(
            painter = painterResource(
                id = image
            ),
            contentDescription = null,
            modifier = Modifier.size(53.dp).padding(10.dp)
        )
        Column (
        ){
            Text(
                text = title,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 20.sp,

            )
            Text(
                text = text,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 17.sp
            )
        }
    }
}