package com.example.smartglass.ui.design

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartglass.data.viewmodel.ViewModelPhoto
import com.example.smartglass.theme.Black
import com.example.smartglass.theme.BlueGray
import com.example.smartglass.ui.design.customizecompose.CustomButtonCompose
import com.example.smartglass.ui.design.customizecompose.CustomImageCompose
import com.example.smartglass.ui.design.customizecompose.CustomSwitchCompose
import com.example.smartglass.ui.design.customizecompose.CustomTextCompose
import com.example.smartglass.ui.design.customizecompose.CustomTextFieldCompose


fun getCustomTextFiledInstance(): CustomTextFieldCompose {
    return CustomTextFieldCompose()
}

fun getCustomTextInstance(): CustomTextCompose {
    return CustomTextCompose()
}

fun getCustomButtonInstance(): CustomButtonCompose {
    return CustomButtonCompose()
}

fun getCustomSwitchCompose(): CustomSwitchCompose {
    return CustomSwitchCompose()
}


@Composable
fun CheckUiColorMode(): Color {
    return if (isSystemInDarkTheme()) BlueGray else Black
}

@Composable
fun CheckUiColorMode2(): Color {
    return if (isSystemInDarkTheme()) Color.White else Black
}


@Composable
fun CheckUiColorButtonMode(): Color {
    return if (isSystemInDarkTheme()) BlueGray else BlueGray
}


fun getCustomImageInstance(): CustomImageCompose {
    return CustomImageCompose()
}



