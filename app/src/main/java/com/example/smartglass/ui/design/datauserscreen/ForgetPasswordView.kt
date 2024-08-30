package com.example.smartglass.ui.design.datauserscreen

import android.content.Context
import android.widget.Toast
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
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.smartglass.R
import com.example.smartglass.utlity.TypeIdUserLogin
import com.example.smartglass.data.viewmodel.FirebaseViewModel
import com.example.smartglass.ui.design.customizecompose.CustomButtonCompose
import com.example.smartglass.ui.design.customizecompose.CustomTextCompose
import com.example.smartglass.ui.design.customizecompose.CustomTextFieldCompose
import com.example.smartglass.ui.design.getCustomButtonInstance
import com.example.smartglass.ui.design.getCustomTextFiledInstance
import com.example.smartglass.ui.design.getCustomTextInstance

class ForgetPasswordView(private val context: Context) {
    private val textFieldCompose: CustomTextFieldCompose = getCustomTextFiledInstance()
    private val textCompose: CustomTextCompose = getCustomTextInstance()
    private val buttonCompose: CustomButtonCompose = getCustomButtonInstance()


    @Composable
    fun forgetPasswordView(navController: NavController, vm: FirebaseViewModel) {
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
                    EmailOrPhoneNumberAndButtonSection(navController, context, vm)


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
                )
                    .CustomizeText(
                        text = stringResource(id = R.string.ForgetPasswordView),
                        modifier = Modifier.padding(vertical = 80.dp),
                    )
            }


        }
    }

    @Composable
    fun EmailOrPhoneNumberAndButtonSection(
        navController: NavController,
        context: Context,
        vm: FirebaseViewModel
    ) {
        // var emailCache  = EmailCache
        var textFieldEmail by remember {
            mutableStateOf("")
        }
        val regexEmail = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
        var isEmailValid by remember { mutableStateOf(true) }
        var errorMessageEmail by remember { mutableStateOf("") }

        textFieldCompose.copy(shape = 8).CustomizeTextField(
            onValueChanges = {
                /*TODO("EmailTextField")*/
                textFieldEmail = it
                isEmailValid = regexEmail.matches(textFieldEmail)
                errorMessageEmail = if (!isEmailValid) "Invalid Email" else ""


            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            label = { textCompose.CustomizeText(text = stringResource(id = R.string.Email)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.AccountBox,
                    contentDescription = null
                )
            },
            isError = !isEmailValid,
        )

        if (errorMessageEmail.isNotEmpty()) {
            textCompose.copy(fontSize = 8, color = Color.Red)
                .CustomizeText(text = errorMessageEmail)
        }

        Spacer(modifier = Modifier.height(8.dp))
        vm.getTypeIdUserLogin(textFieldEmail)
        buttonCompose.copy(shape = 8).CustomizeButton(
            onclick =
            {
                /*TODO("ForgetPasswordButton")*/
                if (TypeIdUserLogin.typeIdUserLogin == 0) {
                    vm.isEmailRegistered(
                        textFieldEmail,
                        context,
                        onSuccess = { isRegistered ->
                            if (isRegistered) {
                                vm.sendPasswordResetEmail(
                                    textFieldEmail,
                                    onSuccess = {
                                        Toast.makeText(
                                            context,
                                            "The Link is send in your Email",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        navController.navigate("/login")
                                        vm.isRestPassword.value = true
                                    }
                                )

                            }
                        }
                    )
                } else {
                    Toast.makeText(context, "The Email dose not sign up ", Toast.LENGTH_SHORT)
                        .show()
                }

            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isEmailValid

        ) {
            textCompose.copy(fontSize = 16)
                .CustomizeText(
                    text = stringResource(id = R.string.ForgetPassword)
                )

        }
    }


}

















