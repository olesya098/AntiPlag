package com.hfad.antiplag.presentation.components.message

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.hfad.antiplag.ui.theme.message
import java.util.Date
import java.util.UUID

//
//@Composable
//fun Message(
//    text: String,
//    modifier: Modifier = Modifier
//) {
//    Column(
//        modifier = modifier
//            .clip(shape = RoundedCornerShape(14.dp))
//            .background(message)
//            .wrapContentWidth()
//    ) {
//        Text(
//            text = text,
//            style = MaterialTheme.typography.bodyMedium,
//        )
//
//
//    }
//}
// model/Message.kt
data class Message(
    val id: String = UUID.randomUUID().toString(),
    val text: String = "",
    val timestamp: Date = Date(),
    val type: MessageType = MessageType.SYSTEM
)

enum class MessageType {
    SYSTEM, USER, ERROR, SUCCESS, WAITING, CHECKING
}

