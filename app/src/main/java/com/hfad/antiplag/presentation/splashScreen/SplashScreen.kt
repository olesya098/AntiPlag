package com.hfad.antiplag.presentation.splashScreen

//import com.hfad.antiplag.navigation.Routes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.hfad.antiplag.R
import com.hfad.antiplag.navigation.Routes
import com.hfad.antiplag.ui.theme.blueLite
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        delay(3400L)
        navController.navigate(Routes.LOGIN) {
            popUpTo(Routes.SPLASHSCREEN) { inclusive = true } // Удаляем SplashScreen из стека
        }
//        navController.navigate(HomeRoute) {
//            popUpTo(SplashRoute) { inclusive = true } // Удаляем SplashScreen из стека
//       }
    }
    // 1. Создаем бесконечную анимацию
    val infiniteTransition = rememberInfiniteTransition()

    // 2. Анимируем масштаб от 0.9 до 1.1
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000, // Длительность 1 секунда
                easing = FastOutSlowInEasing // Плавное ускорение и замедление
            ),
            repeatMode = RepeatMode.Reverse // Движение вперед-назад
        )
    )

    // 3. Анимируем прозрачность от 0.7 до 1.0
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        )
    )
    val arcColor = blueLite
    val arcAngle1 by infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = 180F,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val arcAngle2 by infiniteTransition.animateFloat(
        initialValue = 180F,
        targetValue = 360F,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .weight(1f)  // Занимает все доступное пространство кроме текста
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .padding(12.dp)
                    .size(140.dp)
            ) {
                drawArc(
                    color = arcColor,
                    startAngle = arcAngle1,
                    sweepAngle = 90f,
                    useCenter = false,
                    style = Stroke(width = 10f, cap = StrokeCap.Round),
                )

                drawArc(
                    color = arcColor,
                    startAngle = arcAngle2,
                    sweepAngle = 90f,
                    useCenter = false,
                    style = Stroke(width = 10f, cap = StrokeCap.Round),
                )
            }
            // 4. Применяем анимации к изображению
            Image(
                painter = painterResource(id = R.drawable.iconsheet), // Ваше изображение
                contentDescription = "Pulsating image",
                modifier = Modifier
                    .size(80.dp)
                    .graphicsLayer {
                        scaleX = scale // Применяем анимацию масштаба по X
                        scaleY = scale // Применяем анимацию масштаба по Y
                        this.alpha = alpha // Применяем анимацию прозрачности
                    }
            )
        }
        Text(
            text = "AntiPlag",
            color = blueLite,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 70.dp),
            fontSize = 30.sp
        )
    }
}