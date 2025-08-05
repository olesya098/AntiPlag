package com.hfad.antiplag.presentation.components.bottonBar.filePicker

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hfad.antiplag.R
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.poi.xwpf.extractor.XWPFWordExtractor
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File
import java.io.InputStream

private const val TAG = "FilePicker"

@Composable
fun FilePicker(
    onFile: (Uri) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { onFile(it) }
    }

    Image(
        painter = painterResource(id = R.drawable.files),
        contentDescription = null,
        modifier = Modifier
            .size(35.dp)
            .clickable {
                launcher.launch("*/*")
            }
    )
}

suspend fun Uri.readText(context: Context): String = withContext(Dispatchers.IO) {
    val contentResolver = context.contentResolver
    val mimeType = contentResolver.getType(this@readText) ?: "text/plain"

    contentResolver.openInputStream(this@readText)?.use { inputStream ->
        when (mimeType) {
            "application/pdf" -> readPDF(inputStream)
            "application/msword" -> readDocx(inputStream)
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> readDocx(
                inputStream
            )

            "text/plain" -> readPlainText(inputStream)
            else -> throw Exception("Unsupported file type: $mimeType")
        }
    } ?: throw Exception("Failed to open file")
}

private fun readPlainText(inputStream: InputStream): String {
    return inputStream.bufferedReader().use { it.readText() }
}

private fun readPDF(inputStream: InputStream): String {
    return try {

        var extractedText = ""
        val tempFile = File.createTempFile("temp", ".pdf")
        tempFile.outputStream().use { output ->
            inputStream.copyTo(output)
        }
        val pdfReader = PdfReader(tempFile.absolutePath)

        val n = pdfReader.numberOfPages

        for (i in 0 until n) {
            extractedText =
                """
                 $extractedText${
                    PdfTextExtractor.getTextFromPage(pdfReader, i + 1).trim { it <= ' ' }
                }
                 """.trimIndent()
        }

        pdfReader.close()
        extractedText

    } catch (e: Exception) {
        Log.e(TAG, "Read pdf: ${e.message}")
        e.toString()
    }
}

fun readDocx(inputStream: InputStream): String {
    return try {

        val docx = XWPFDocument(inputStream)
        val wx = XWPFWordExtractor(docx)
        wx.text

    } catch (e: Exception) {
        Log.e(TAG, "Read docx: ${e.message}")
        e.toString()
    }
}
