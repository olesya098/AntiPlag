package com.hfad.antiplag.presentation.components.customTextFieldPassword

import android.R.attr.visible
import android.graphics.Color.blue
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.hfad.antiplag.R
import com.hfad.antiplag.ui.theme.blueLite


@Composable
fun CustomTextFieldPassword(
    value: String,
    visible: Boolean,
    onvalChange: (String) -> Unit,
    label: String,
    onVisibilityChange: (Boolean) -> Unit,
) {
    TextField(
        value = value,
        onValueChange = { newText ->
            // Удаляем все пробелы из вводимого текста
            val filteredText = newText.replace("\\s".toRegex(), "")
            onvalChange(filteredText)
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
                shape = RoundedCornerShape(6.dp)
            )
            .height(58.dp),
        shape = RoundedCornerShape(32.dp),
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)

            )
        },
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            Icon(
                painter = painterResource(
                    if (visible) R.drawable.eye else R.drawable.eye_off
                ),
                contentDescription = if (visible) "Hide password" else "Show password",
                modifier = Modifier
                    .clickable { onVisibilityChange(!visible) }
                    .size(40.dp)
                    .padding(end = 10.dp),
                tint = blueLite
            )
        },
    )
}