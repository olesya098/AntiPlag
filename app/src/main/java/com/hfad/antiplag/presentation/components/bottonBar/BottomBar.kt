package com.hfad.antiplag.presentation.components.bottonBar

import android.R.attr.label
import android.R.id.message
import android.util.Log
import android.util.Log.d
import android.util.Log.e
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hfad.antiplag.R
import com.hfad.antiplag.presentation.components.bottonBar.filePicker.FilePicker
import com.hfad.antiplag.presentation.components.bottonBar.filePicker.readText
import com.hfad.antiplag.ui.theme.blueLite
import com.hfad.antiplag.ui.theme.liteGray
import kotlinx.coroutines.launch

@Composable
fun BottomBar() {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var message by remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilePicker(
            onFile = {
                scope.launch {
                    try {
                        val text = it.readText(context)
                        Log.d("FilePicker", text)

                    } catch (e: Exception) {
                        Log.e("My", "$e Error")
                    }

                }
            }
        )

        TextField(
            value = message,
            onValueChange = {
                message = it
            },
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.primary,
                unfocusedTextColor = MaterialTheme.colorScheme.primary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
            ),
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = blueLite,
                    shape = RoundedCornerShape(32.dp)
                )
                .height(56.dp),
            shape = RoundedCornerShape(32.dp),
            label = {
                Text(
                    text = stringResource(R.string.message),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)

                )
            },
            trailingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.send),
                    contentDescription = null,
                    modifier = Modifier
                        .size(35.dp)
                        .padding(end = 5.dp)
                )
            }
        )

    }
}