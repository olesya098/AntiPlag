package com.hfad.antiplag.presentation.result

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hfad.antiplag.R
import com.hfad.antiplag.navigation.Routes
import com.hfad.antiplag.presentation.components.customScaffold.CustomScaffold
import com.hfad.antiplag.viewModel.PlagiarismCheckViewModel

@Composable
fun ResultScreen(navController: NavController, plagiarismCheckViewModel: PlagiarismCheckViewModel) {
//    val plagiarismCheckViewModel: PlagiarismCheckViewModel = viewModel()
//    val reportState = plagiarismCheckViewModel.report.collectAsState()
//
//    val matched = (reportState.value?.data?.reportData?.matchedPercent ?: 0).coerceIn(0, 100)
//    val original = (100 - matched).coerceIn(0, 100)
//
//    CustomScaffold(
//        title = stringResource(R.string.app_name),
//        actions = {
//            Image(
//                painter = painterResource(id = R.drawable.information),
//                contentDescription = "About",
//                modifier = Modifier
//                    .size(27.dp)
//                    .clickable {
//                        navController.navigate(Routes.ABOUT)
//                    }
//            )
//        },
//        navigationIcon = {
//            Image(
//                painter = painterResource(id = R.drawable.navigate_before),
//                contentDescription = null,
//                modifier = Modifier
//                    .size(24.dp)
//                    .clickable {
//                        navController.navigate(Routes.HOME)
//                    }
//
//
//            )
//        }
//    ) { scaffoldPadding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(MaterialTheme.colorScheme.background)
//                .padding(scaffoldPadding),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            PieChart(
//                plagiarismPercent = matched,
//                originalityPercent = original,
//                modifier = Modifier.size(220.dp)
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
//                Text(text = "● Плагиат: $matched%", color = Color(0xFFE53935), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
//                Text(text = "● Оригинальность: $original%", color = Color(0xFF43A047), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
//            }
//        }
//    }
//}
//
//@Composable
//private fun PieChart(
//    plagiarismPercent: Int,
//    originalityPercent: Int,
//    modifier: Modifier = Modifier,
//    plagiarismColor: Color = Color(0xFFE53935), // красный
//    originalityColor: Color = Color(0xFF43A047) // зелёный
//) {
//    val safePlag = plagiarismPercent.coerceIn(0, 100)
//    val safeOrig = originalityPercent.coerceIn(0, 100)
//    val total = (safePlag + safeOrig).takeIf { it > 0 } ?: 1
//
//    val startAngle = -90f
//    val plagSweep = 360f * (safePlag / total.toFloat())
//    val origSweep = 360f - plagSweep
//
//    Canvas(modifier = modifier) {
//        drawArc(
//            color = plagiarismColor,
//            startAngle = startAngle,
//            sweepAngle = plagSweep,
//            useCenter = true
//        )
//        drawArc(
//            color = originalityColor,
//            startAngle = startAngle + plagSweep,
//            sweepAngle = origSweep,
//            useCenter = true
//        )
//    }
}