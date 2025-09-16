package com.hfad.antiplag.presentation.components.bottonBar

import android.R.attr.label
import android.R.id.message
import android.content.Context
import android.net.Uri
import android.util.Log
import android.util.Log.d
import android.util.Log.e
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hfad.antiplag.R
import com.hfad.antiplag.presentation.components.bottonBar.filePicker.FilePicker
import com.hfad.antiplag.presentation.components.bottonBar.filePicker.readText
import com.hfad.antiplag.presentation.components.message.MessageType
import com.hfad.antiplag.ui.theme.blueLite
import com.hfad.antiplag.ui.theme.coolBlack
import com.hfad.antiplag.ui.theme.liteGray
import com.hfad.antiplag.viewModel.LoginSigninViewModel
import com.hfad.antiplag.viewModel.MessageViewModel
import com.hfad.antiplag.viewModel.PlagiarismCheckViewModel
import kotlinx.coroutines.launch
// components/bottonBar/BottomBar.kt
@Composable
fun BottomBar(
    plagiarismCheckViewModel: PlagiarismCheckViewModel,
    messageViewModel: MessageViewModel, // Добавляем MessageViewModel
    viewModel: LoginSigninViewModel // Добавляем для получения userId
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var message by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var fileName by remember { mutableStateOf("") }

    Column {
        if (selectedFileUri != null) {
            FileAttachmentCard(
                fileName = fileName,
                onRemove = {
                    selectedFileUri = null
                    fileName = ""
                    text = ""
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilePicker(
                onFile = { uri ->
                    selectedFileUri = uri
                    fileName = getFileNameFromUri(context, uri) ?: "Unknown file"
                    scope.launch {
                        try {
                            text = uri.readText(context)
                            Log.d("FilePicker", text)
                        } catch (e: Exception) {
                            Log.e("My", "$e Error")
                            fileName = "Error reading file"
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
                            .clickable {
                                if (message.isNotBlank()) {
                                    // Сохраняем пользовательское сообщение
                                    val userId = viewModel.getCurrentUserId()
                                    if (userId.isNotEmpty()) {
                                        try {
                                            messageViewModel.addMessage(userId, message, MessageType.USER)
                                        } catch (e: Exception) {
                                            messageViewModel.addLocalMessage(message, MessageType.USER)
                                        }
                                    } else {
                                        messageViewModel.addLocalMessage(message, MessageType.USER)
                                    }

                                    // Отправляем текст на проверку
                                    plagiarismCheckViewModel.checkText(text = processText(message.trimIndent()))

                                    // Очищаем поле ввода
                                    message = ""
                                } else if (text.isNotBlank()) {
                                    // Обработка текста из файла
                                    Log.d("Final text", processText(text.trimIndent()))
                                    plagiarismCheckViewModel.checkText(text = processText(text.trimIndent()))
                                }
                            }
                    )
                }
            )
        }
    }
}

@Composable
fun FileAttachmentCard(
    fileName: String,
    onRemove: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    Box(
        modifier = Modifier
            .width(screenWidth * 0.55f)
            .padding(horizontal = 16.dp)
            .background(
                color = MaterialTheme.colorScheme.scrim,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 13.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.document),
                    contentDescription = stringResource(R.string.attached_file),
                    modifier = Modifier.size(25.dp)
                )

                Text(
                    text = fileName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = coolBlack,
                    maxLines = 1,
                    fontSize = 15.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                )
            }

            Image(
                painter = painterResource(id = R.drawable.close),
                contentDescription = stringResource(R.string.remove_file),
                modifier = Modifier
                    .size(18.dp)
                    .clickable {
                        onRemove()
                    }
            )
        }
    }

}

fun getFileNameFromUri(context: Context, uri: Uri): String? {
    return try {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayNameIndex = it.getColumnIndex("_display_name")
                if (displayNameIndex != -1) {
                    it.getString(displayNameIndex)
                } else {
                    uri.path?.substringAfterLast('/')
                }
            } else {
                uri.path?.substringAfterLast('/')
            }
        } ?: uri.path?.substringAfterLast('/')
    } catch (e: Exception) {
        Log.e("FilePicker", "Error getting file name: ${e.message}")
        uri.path?.substringAfterLast('/')
    }
}

fun processText(input: String): String {
    return input.lines()
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .joinToString(" ")
        .replace(Regex("\\*\\*"), "")
        .replace(Regex("\\[\\[.*?]]"), "")
        .replace(regex = Regex("\\{.*?\\}"), replacement = "")
        .replace(Regex("\\[.*?]\\(.*?\\)"), "")
        .replace(Regex("\\s+"), " ")
        .trim()
}