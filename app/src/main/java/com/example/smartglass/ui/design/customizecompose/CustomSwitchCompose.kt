package com.example.smartglass.ui.design.customizecompose

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

data class CustomSwitchCompose(
    private var colorSwitch: Color = Color.Green
) {

    @Composable
    fun CustomizeSwitchImage(
        modifier: Modifier = Modifier,
        onCheckedChange: ((Boolean) -> Unit)?,
    ) {
        var checkedState by remember { mutableStateOf(false) }
        Switch(
            checked = checkedState,
            onCheckedChange = { newCheck ->
                checkedState = newCheck
                if (onCheckedChange != null) {
                    onCheckedChange(newCheck)
                }
            },
            colors = SwitchDefaults.colors(checkedTrackColor = colorSwitch)
        )


    }

}