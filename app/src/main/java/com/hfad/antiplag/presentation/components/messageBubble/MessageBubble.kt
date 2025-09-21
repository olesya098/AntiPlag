import android.R.attr.text
import android.R.attr.textColor
import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.util.copy
import com.hfad.antiplag.R
import com.hfad.antiplag.presentation.components.message.MessageType
import com.hfad.antiplag.ui.theme.blueLite
import com.hfad.antiplag.ui.theme.grayDeviderLite
import com.itextpdf.text.pdf.security.LtvTimestamp.timestamp
import java.util.Date
@Composable
fun MessageBubble(
    message: String,
    timestamp: Date,
    type: MessageType = MessageType.SYSTEM,
    modifier: Modifier = Modifier,
) {
    val isUserMessage = type == MessageType.USER

    val backgroundColor = when (type) {
        MessageType.USER -> blueLite.copy(alpha = 0.1f)
        MessageType.ERROR -> MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
        MessageType.SUCCESS -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        else -> grayDeviderLite
    }

    val textColor = when (type) {
        MessageType.USER -> blueLite
        MessageType.ERROR -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurface
    }

    val shape = when (type) {
        MessageType.USER ->
            RoundedCornerShape(
                topStart = 12.dp,
                topEnd = 1.dp,
                bottomStart = 12.dp,
                bottomEnd = 12.dp
            )
        else ->
            RoundedCornerShape(
                topStart = 1.dp,
                topEnd = 12.dp,
                bottomStart = 12.dp,
                bottomEnd = 12.dp
            )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        if (isUserMessage) {
            // Сообщение пользователя - справа
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = modifier
                    .wrapContentWidth()
                    .background(backgroundColor, shape)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor
                )
            }
        } else {
            // Сообщение системы - слева
            Box(
                modifier = modifier
                    .wrapContentWidth()
                    .background(backgroundColor, shape)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}


//@Composable
//fun MessageBubble(
//    message: String,
//    timestamp: Date,
//    type: MessageType = MessageType.SYSTEM, // Добавляем параметр типа
//    modifier: Modifier = Modifier,
//) {
//    val backgroundColor = when (type) {
//        MessageType.USER -> blueLite.copy(alpha = 0.1f)
//        MessageType.ERROR -> MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
//        MessageType.SUCCESS -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
//        else -> grayDeviderLite
//    }
//
//    val textColor = when (type) {
//        MessageType.USER -> blueLite
//        MessageType.ERROR -> MaterialTheme.colorScheme.error
//        else -> MaterialTheme.colorScheme.onSurface
//    }
//    val shape = when (type) {
//        MessageType.USER ->
//            RoundedCornerShape(
//                topStart = 12.dp,
//                topEnd = 1.dp,
//                bottomStart = 12.dp,
//                bottomEnd = 12.dp
//            )
//
//        else ->
//            RoundedCornerShape(
//            topStart = 1.dp,
//            topEnd = 12.dp,
//            bottomStart = 12.dp,
//            bottomEnd = 12.dp
//        )
//    }
//
//    Box(
//        modifier = modifier
//            .wrapContentWidth()
//            .background(backgroundColor, shape)
//            .padding(horizontal = 16.dp, vertical = 12.dp)
//    ) {
//        Text(
//            text = message,
//            style = MaterialTheme.typography.bodyMedium,
//            color = textColor
//        )
//
//    }
//}

//@Preview
//@Composable
//fun MessagePreview() {
//    MessageBubble(
//        message = "Привет!"
//    )
//}