package com.hfad.antiplag.presentation.components.dialog

import androidx.compose.foundation.clickable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Dialog(message: String, onDismiss: () -> Unit, ) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Уведомление") },
        text = { Text(message) },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK",)
            }
        }
    )
}