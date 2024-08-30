package com.example.smartglass.ui.design.customizecompose

import androidx.compose.foundation.layout.padding
//import androidx.compose.material.Text
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class CustomTextCompose(
    private var fontSize: Int = 10,
    private var fontStyle: FontStyle = FontStyle.Normal,
    private var fontWeight: FontWeight = FontWeight.Normal,
    private var fontFamily: FontFamily = FontFamily.Default,
    private var horizontalPadding: Int = 0,
    private var verticalPadding: Int = 0,
    private var color: Color = Color.Unspecified,
    private var contentDescriptions:String = ""
    ) {


    @Composable
    fun CustomizeText(text: String, modifier: Modifier = Modifier) {
        Text(
            text = text,
            color = color,
            fontSize = fontSize.sp,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            modifier = modifier
                .padding(
                    horizontal = horizontalPadding.dp,
                    vertical = verticalPadding.dp
                )
                .semantics { contentDescription = contentDescriptions },

            )
    }

    @Composable
    fun CustomizeTextButton(text: String, modifier: Modifier = Modifier) {
        Text(
            text = text,
            fontSize = fontSize.sp,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            modifier = modifier
                .padding(
                    horizontal = horizontalPadding.dp,
                    vertical = verticalPadding.dp
                )
                .semantics { contentDescription = contentDescriptions },
            color = color,

            )
    }

    @Composable
    fun CustomizeTextImage(text: String, modifier: Modifier = Modifier) {
        Text(
            text = text,
            fontSize = fontSize.sp,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            modifier = modifier
                .padding(
                    horizontal = horizontalPadding.dp,
                    vertical = verticalPadding.dp
                )
                .semantics { contentDescription = contentDescriptions },
            color = color,

            )
    }
}
