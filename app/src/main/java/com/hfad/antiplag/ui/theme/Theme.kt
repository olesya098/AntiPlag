package com.hfad.antiplag.ui.theme

import android.R.color.white
import android.app.Activity
import android.graphics.Color.blue
import android.graphics.Color.green
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    background = coolBlack, //задний фон
    primary = com.hfad.antiplag.ui.theme.white, //title
//    secondary = white.copy(alpha = 0.6f), //subtitle
    surface = darkGray.copy(alpha = 0.5f),
//    onPrimary = deepBlue,
    scrim = grayDevider,//buttom
//    outline = orange,//оранжевый
//    inverseOnSurface = Color(0xFF525661), //точка
//    surfaceContainerLow = white.copy(alpha = 0.6f),//skip onboarding
//    inversePrimary = white.copy(alpha = 0.6f), // РЯДОМ С TEXT FIELD
//    surfaceContainer = smallText, //текст который ввожу
//    inverseSurface = white.copy(alpha = 0.8f),//Цвет text field
//    onSurfaceVariant = grayDark.copy(alpha = 0.5f),//placeHolder
//    error = pink,//forgor password
//    onErrorContainer = grayDark, //ты не участник?
//    onTertiaryContainer = Color(0xFF000000),//mother language color text
//    surfaceContainerLowest = beige,//для карточек выбора языка
//    surfaceContainerHighest = Color(0xFFD9D9D9),//Круг где должен быть image user`а
//    primaryContainer = lightLightGray, //top user
//    onSecondaryContainer = green,//зелёный
//    surfaceContainerHigh = Color(0xFFFFFFFF), //текст
//    tertiary = Color(0xFF1B2336), //для фона на странице выбора фото
//    tertiaryContainer = gray,//текст карточки wordpractice
//    onTertiary = purple

)

//СВЕТЛАЯ
private val LightColorScheme = lightColorScheme(
    background = com.hfad.antiplag.ui.theme.white,
    primary = coolBlack, //title
//    secondary = bigText.copy(alpha = 0.6f), //subtitle
    surface = liteGray.copy(alpha = 0.3f),
//    onPrimary = deepBlue,
    scrim = grayDeviderLite,//buttom
//    outline = orange,//оранжевый
//    inverseOnSurface = Color(0xFFCECFD2), //точка
//    surfaceContainerLow = bigText,//skip onboarding
//    inversePrimary = smallText, // РЯДОМ С TEXT FIELD
//    surfaceContainer = smallText, //текст который ввожу
//    inverseSurface = bigText.copy(alpha = 0.08f),//Цвет text field
//    onSurfaceVariant = grayDark.copy(alpha = 0.5f),//placeHolder
//    error = pink,//forgor password
//    onErrorContainer = grayDark, //ты не участник?
//    onTertiaryContainer = Color(0xFF000000),//mother language color text
//    surfaceContainerLowest = beige,//для карточек выбора языка
//    surfaceContainerHighest = Color(0xFFD9D9D9),//Круг где должен быть image user`а
//    primaryContainer = lightLightGray, //top user
//    onSecondaryContainer = green,//зелёный
//    surfaceContainerHigh = Color(0xFF000000), //текст
//    tertiary = Color(0xFF1B2336), //для фона на странице выбора фото
//    tertiaryContainer = gray,//текст карточки wordpractice
//    onTertiary = purple


)

@Composable
fun AntiPlagTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
   val colors = if (darkTheme){
       DarkColorScheme
   }else{
       LightColorScheme
   }

    MaterialTheme(
        content = content,
        colorScheme = colors,
        typography = Typography,
    )
}