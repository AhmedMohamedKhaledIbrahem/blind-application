package com.example.smartglass.ui.design.datauserscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.smartglass.R
import com.example.smartglass.ui.design.CheckUiColorMode2
import com.example.smartglass.ui.design.customizecompose.CustomButtonCompose
import com.example.smartglass.ui.design.customizecompose.CustomTextCompose
import com.example.smartglass.ui.design.customizecompose.CustomTextFieldCompose
import com.example.smartglass.ui.design.getCustomButtonInstance
import com.example.smartglass.ui.design.getCustomTextFiledInstance
import com.example.smartglass.ui.design.getCustomTextInstance

class PasswordChangeView {
    private val textFieldCompose: CustomTextFieldCompose = getCustomTextFiledInstance()
    private val textCompose: CustomTextCompose = getCustomTextInstance()
    private val buttonCompose: CustomButtonCompose = getCustomButtonInstance()


    @Composable
    fun passwordChangeView(navController: NavController) {
        Surface {
            Column(
                Modifier.fillMaxSize()
            ) {
                LogoAndShapeAndTextIntroductionSection()
                Spacer(modifier = Modifier.height(15.dp))
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 30.dp)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    NewPasswordANDConfirmPasswordAndOtpAndButtonSection(navController)


                }

            }

        }

    }

    @Composable
    fun LogoAndShapeAndTextIntroductionSection() {
        Box(contentAlignment = Alignment.TopCenter) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fraction = 0.35f),
                painter = painterResource(id = R.drawable.shape),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                textCompose.copy(
                    fontSize = 30,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                ).CustomizeText(
                    text = stringResource(id = R.string.ChangePassword),
                    modifier = Modifier.padding(vertical = 80.dp),
                )
            }


        }
    }

    @Composable
    fun NewPasswordANDConfirmPasswordAndOtpAndButtonSection(navController: NavController) {
        var textFieldOtp by remember {
            mutableStateOf("")
        }
        var textFieldNewPassword by remember {
            mutableStateOf("")
        }
        var textFieldConfirmPassword by remember {
            mutableStateOf("")
        }



        textFieldCompose.copy(shape = 8).CustomizeTextField(
            onValueChanges = {
                /*TODO("OTPTextField")*/
                textFieldOtp = it
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            label = { textCompose.CustomizeText(text = stringResource(id = R.string.Otp)) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_send_to_mobile_24),
                    contentDescription = null
                )
            },
        )

        Spacer(modifier = Modifier.height(8.dp))

        textFieldCompose.copy(shape = 8).CustomizeTextField(
            onValueChanges = {
                /*TODO("NewPasswordTextField")*/
                textFieldNewPassword = it
            },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
            ),
            label = {
                textCompose.CustomizeText(
                    text = stringResource(id = R.string.NewPasswordChange)
                )
            },
            leadingIcon = { Icon(imageVector = Icons.Filled.Lock, contentDescription = null) },
        )

        Spacer(modifier = Modifier.height(8.dp))

        textFieldCompose.copy(shape = 8).CustomizeTextField(
            onValueChanges = {
                /*TODO("ConfirmPasswordTextField")*/
                textFieldConfirmPassword = it
            },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
            ),
            label = {
                textCompose.CustomizeText(
                    text = stringResource(id = R.string.ConfirmPassword)
                )
            },
            leadingIcon = { Icon(imageVector = Icons.Filled.Lock, contentDescription = null) },
        )

        Spacer(modifier = Modifier.height(8.dp))

        buttonCompose.copy(shape = 8).CustomizeButton(
            onclick = { /*TODO("ChangePasswordButton")*/

                navController.navigate("/login")
            }, modifier = Modifier.fillMaxWidth()
        ) {
            textCompose.copy(fontSize = 16).CustomizeText(
                text = stringResource(id = R.string.ChangePassword)
            )

        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = {/*TODO("Send OTP Button")*/

        }) {
            textCompose.copy(fontSize = 15, color = CheckUiColorMode2())
                .CustomizeTextButton(text = stringResource(id = R.string.SendOTPAGIAN))

        }


    }


}