package com.hfad.antiplag.presentation.components.bottonBar.filePicker

import android.R.attr.text
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
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.poi.xwpf.extractor.XWPFWordExtractor
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.InputStream
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
    val supportetTypes = listOf(
        "application/pdf",
        "application/msword",
        "text/plain",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    )

    Image(
        painter = painterResource(id = R.drawable.files),
        contentDescription = null,
        modifier = Modifier
            .size(35.dp)
            .clickable {
                launcher.launch(supportetTypes.joinToString("|"))
            }
    )

}

suspend fun Uri.readText(context: Context): String = withContext(Dispatchers.IO) {
    val type = context.contentResolver.getType(this@readText) ?: "text/plain"
    context.contentResolver.openInputStream(this@readText)?.use {
        when (type) {
            "application/pdf" -> readPDF(it)
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> readDocx(it)
            "text/plain" -> readPlaneText(it)
            else -> throw Exception("unsupported type")
        }
    }?: throw Exception("failed file open")
}

private fun readPlaneText(inputStream: InputStream): String {
    return inputStream.bufferedReader().use {
        it.readText()
    }
}

private fun readPDF(inputStream: InputStream): String {
    return try {
        PDDocument.load(inputStream).use {
            val stripper = PDFTextStripper()
            stripper.getText(it)
        }

    } catch (e: Exception) {
        throw Exception("Failed to read PDF $e")
    }
}

private fun readDocx(inputStream: InputStream): String {
    return try {
        XWPFDocument(inputStream).use {
            XWPFWordExtractor(it).text
        }
    } catch (e: Exception) {
        throw Exception("Error $e")
    }
}