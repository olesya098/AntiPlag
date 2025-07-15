package com.hfad.antiplag.presentation.components.bottonBar.filePicker

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hfad.antiplag.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URI

@Composable
fun FilePicker(
    onFile: (Uri) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let { its ->
            onFile(its)

        }
    }

    Image(
        painter = painterResource(id = R.drawable.files),
        contentDescription = null,
        modifier = Modifier
            .size(35.dp)
            .clickable {
                launcher.launch("text/plain")
            }
    )

}

suspend fun Uri.readText(context: Context): String = withContext(Dispatchers.IO) {
    context.contentResolver.openInputStream(this@readText)?.use {
        it.bufferedReader().use {
            it.readText()
        }
    } ?: throw Exception("Не удалось открыть файл")
}