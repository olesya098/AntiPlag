package com.hfad.antiplag.ui.theme

import android.R.attr.fontFamily
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hfad.antiplag.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
    //title
//    bodyMedium = TextStyle(
//        fontFamily = FontFamily(Font(R.font.sf_pro_display_semibold)),
//    ),
//    bodySmall = TextStyle(
//        fontFamily = FontFamily(Font(R.font.sf_pro_display_medium)),
//    ),
//    labelMedium = TextStyle(
//        fontFamily = FontFamily(Font(R.font.sf_pro_display_medium_italic)),
//    ),
//    labelSmall = TextStyle(
//        fontFamily = FontFamily(Font(R.font.sf_pro_display_light_italic))
//    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.tttravelsdemibold)),
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.tttravelsmedium)),
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.tttravelsmediumitalic)),
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.tttravelsextralightitalic))
    )
)