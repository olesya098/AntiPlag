package com.hfad.antiplag.presentation.result

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hfad.antiplag.R
import com.hfad.antiplag.navigation.Routes
import com.hfad.antiplag.presentation.components.customScaffold.CustomScaffold
import com.hfad.antiplag.ui.theme.AntiPlagTheme
import com.hfad.antiplag.viewModel.PlagiarismCheckViewModel
import org.slf4j.helpers.Util.report

@Composable
fun ResultScreen(navController: NavController, plagiarismCheckViewModel: PlagiarismCheckViewModel) {
    val reportState = plagiarismCheckViewModel.report.collectAsState()

    val context = LocalContext.current
    val matched = (reportState.value?.data?.report?.percent?.toDouble()?.toInt() ?: 0)
    val original = (100 - matched)

    CustomScaffold(
        title = stringResource(R.string.app_name),
        actions = {
            Image(
                painter = painterResource(id = R.drawable.information),
                contentDescription = "About",
                modifier = Modifier
                    .size(27.dp)
                    .clickable {
                        navController.navigate(Routes.ABOUT)
                    }
            )
        },
        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.navigate_before),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        navController.navigate(Routes.HOME)
                    }


            )
        }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(scaffoldPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            PieChart(
                plagiarismPercent = matched,
                originalityPercent = original,
                modifier = Modifier
                    .size(220.dp)
                    .padding(top = 60.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "● Плагиат: $matched%",
                    color = Color(0xFFE53935),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "● Оригинальность: $original%",
                    color = Color(0xFF43A047),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            reportState.value?.let { report ->
                LazyColumn(
                ) {
                    items(report.data.reportData.sources) {
                        Text(
                            text = it.source,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.clickable {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.source))
                                context.startActivity(intent)
                            }
                        )
                    }


                }
            }
        }
    }
}

@Composable
private fun PieChart(
    plagiarismPercent: Int,
    originalityPercent: Int,
    modifier: Modifier = Modifier,
    plagiarismColor: Color = Color(0xFFE53935),
    originalityColor: Color = Color(0xFF43A047),
    backgroundColor: Color = Color.LightGray.copy(alpha = 0.3f)
) {
    val safePlag = plagiarismPercent.coerceIn(0, 100)
    val safeOrig = originalityPercent.coerceIn(0, 100)

    // Для равномерного круга всегда используем 360 градусов
    val startAngle = -90f
    val plagSweep = 360f * (safePlag / 100f)
    val origSweep = 360f * (safeOrig / 100f)

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.minDimension / 2

            // Фоновый круг
            drawCircle(
                color = backgroundColor,
                radius = radius,
                center = center
            )

            // Круг плагиата (красный)
            drawArc(
                color = plagiarismColor,
                startAngle = startAngle,
                sweepAngle = plagSweep,
                useCenter = true,
                size = Size(radius * 2, radius * 2),
                topLeft = Offset(center.x - radius, center.y - radius)
            )

            // Круг оригинальности (зеленый)
            drawArc(
                color = originalityColor,
                startAngle = startAngle + plagSweep,
                sweepAngle = origSweep,
                useCenter = true,
                size = Size(radius * 2, radius * 2),
                topLeft = Offset(center.x - radius, center.y - radius)
            )
        }

    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun SimpleResultScreenPreview() {
    AntiPlagTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(start = 16.dp, end = 16.dp, top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            PieChart(
                plagiarismPercent = 35,
                originalityPercent = 65,
                modifier = Modifier
                    .size(220.dp)

            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "● Плагиат: 35%",
                    color = Color(0xFFE53935),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "● Оригинальность: 65%",
                    color = Color(0xFF43A047),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(
                    listOf(
                        "https://example.com/source1",
                        "https://example.com/source2",
                        "https://example.com/source3"
                    )
                ) { source ->
                    Text(
                        text = source,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}