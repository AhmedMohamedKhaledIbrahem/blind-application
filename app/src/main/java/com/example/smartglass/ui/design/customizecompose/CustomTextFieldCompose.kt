package com.example.smartglass.ui.design.customizecompose

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartglass.theme.focusedTextFieldText
import com.example.smartglass.theme.textFieldTextContainer
import com.example.smartglass.theme.unfocusedTextFieldText


data class CustomTextFieldCompose(
    private var fontSize: Int = 18,
    private var fontWeight: FontWeight = FontWeight.Normal,
    private var fontColor: Color = Color.Black,
    private var fontStyle: FontStyle = FontStyle.Normal,
    private var shape: Int = 0,
    private var backgroundColor: Color = Color.White,
    private var cursorColor: Color = Color.Black,
    private var horizontalPadding: Int = 0,
    private var verticalPadding: Int = 0,
    private var minWidth: Int = 280,
    private var minHeight: Int = 56,
    private var width: Int = 280,
    private var height: Int = 56,
    private var contentDescriptions: String = "",
) {


    @Composable
    fun CustomizeTextField(
        onValueChanges: (String) -> Unit,
        leadingIcon: @Composable (() -> Unit)? = null,
        trailingIcon: @Composable (() -> Unit)? = null,
        label: @Composable (() -> Unit)? = null,
        modifier: Modifier = Modifier,
        visualTransformation: VisualTransformation = VisualTransformation.None,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
        isError: Boolean = false,

        ) {

        var textFiled by remember { mutableStateOf("") }

        TextField(

            value = textFiled,
            onValueChange = { newValue ->
                textFiled = newValue
                onValueChanges(newValue)
            },
            modifier = modifier
                .padding(
                    horizontal = horizontalPadding.dp,
                    vertical = verticalPadding.dp,
                )
                .defaultMinSize(
                    minWidth = minWidth.dp,
                    minHeight = minHeight.dp
                )
                .width(width.dp)
                .height(height.dp)
                .semantics { contentDescription = contentDescriptions },


            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            textStyle = TextStyle(
                fontSize = fontSize.sp,
                fontWeight = fontWeight,
                //color= fontColor ,
                fontStyle = fontStyle,
            ),
            shape = RoundedCornerShape(shape.dp),
            colors = TextFieldDefaults.colors(
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.unfocusedTextFieldText,
                focusedPlaceholderColor = MaterialTheme.colorScheme.focusedTextFieldText,
                unfocusedContainerColor = MaterialTheme.colorScheme.textFieldTextContainer,
                focusedContainerColor = MaterialTheme.colorScheme.textFieldTextContainer,
            ),
            label = label,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = isError,


            )
    }

    @Composable
    fun CustomizeOutLinedTextFiled(

        leadingIcon: @Composable (() -> Unit)? = null,
        trailingIcon: @Composable (() -> Unit)? = null,
        label: @Composable (() -> Unit)? = null,
        onValueChanges: (String) -> Unit,
        modifier: Modifier = Modifier,
        visualTransformation: VisualTransformation = VisualTransformation.None,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
    ) {
        val textFiled = remember { mutableStateOf("") }
        OutlinedTextField(
            value = textFiled.value,
            modifier = modifier
                .padding(
                    horizontal = horizontalPadding.dp,
                    vertical = verticalPadding.dp
                )
                .defaultMinSize(
                    minWidth = minWidth.dp,
                    minHeight = minHeight.dp
                )
                .width(width.dp)
                .height(height.dp)
                .semantics { contentDescription = contentDescriptions },
            onValueChange = { value ->
                textFiled.value = value
                onValueChanges(value)
            },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            textStyle = TextStyle(
                fontSize = fontSize.sp,
                fontWeight = fontWeight,
                color = fontColor,
                fontStyle = fontStyle,
            ),
            shape = RoundedCornerShape(shape.dp),
            colors = TextFieldDefaults.colors(
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.unfocusedTextFieldText,
                focusedPlaceholderColor = MaterialTheme.colorScheme.focusedTextFieldText,
                unfocusedContainerColor = MaterialTheme.colorScheme.textFieldTextContainer,
                focusedContainerColor = MaterialTheme.colorScheme.textFieldTextContainer,
            ),
            label = label,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,


            )

    }

}