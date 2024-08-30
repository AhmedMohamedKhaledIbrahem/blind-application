package com.example.smartglass.ui.design.customizecompose

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.smartglass.ui.design.CheckUiColorMode
import com.example.smartglass.ui.design.getCustomTextInstance

data class CustomButtonCompose(
    private var horizontalPadding: Int = 0,
    private var verticalPadding: Int = 0,
    private var minWidth: Int = 64,
    private var minHeight: Int = 36,
    private var shape: Int = 0,
    private var contentDescriptions :String =""


    ) {
    private val textCompose: CustomTextCompose = getCustomTextInstance()

    @Composable
    fun CustomizeButton(
        onclick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        content: @Composable RowScope.() -> Unit,
    ) {

        Button(
            onClick = onclick,
            modifier = modifier
                .padding(horizontal = horizontalPadding.dp, vertical = verticalPadding.dp)
                .defaultMinSize(minWidth = minWidth.dp, minHeight = minHeight.dp)
                .semantics { contentDescription = contentDescriptions  }

            ,
            colors = ButtonDefaults.buttonColors(
                containerColor = CheckUiColorMode(),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(shape.dp),
            enabled = enabled,


        ) {
            content()
        }


    }

    @Composable
    fun CustomizeOutLinedButton(
        onclick: () -> Unit,
        modifier: Modifier = Modifier,
        content: @Composable RowScope.() -> Unit,

        ) {

        OutlinedButton(
            onClick = onclick,
            modifier = modifier
                .padding(horizontal = horizontalPadding.dp, vertical = verticalPadding.dp)
                .defaultMinSize(minWidth = minWidth.dp, minHeight = minHeight.dp)
                .semantics { contentDescription = contentDescriptions  },
            colors = ButtonDefaults.buttonColors(containerColor = CheckUiColorMode()),
            shape = RoundedCornerShape(shape.dp),


            ) {
            content()
        }


    }


    @Composable
    fun CustomizeTextButton(
        onclick: () -> Unit,
        modifier: Modifier = Modifier,
        content: @Composable RowScope.() -> Unit,
        //text:String
    ) {
        TextButton(
            onClick = onclick,
            modifier = modifier
                .padding(horizontal = horizontalPadding.dp, vertical = verticalPadding.dp)
                .defaultMinSize(minWidth = minWidth.dp, minHeight = minHeight.dp)
                .semantics { contentDescription = contentDescriptions  },
            shape = RoundedCornerShape(shape.dp),
            content = content,

            )

    }


}
