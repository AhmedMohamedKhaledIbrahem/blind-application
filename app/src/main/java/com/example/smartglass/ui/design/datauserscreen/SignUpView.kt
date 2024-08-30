package com.example.smartglass.ui.design.datauserscreen

import android.content.Context
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.example.smartglass.R
import com.example.smartglass.data.model.UserProfileModel
import com.example.smartglass.data.viewmodel.FirebaseViewModel
import com.example.smartglass.data.viewmodel.UserProfileViewModel
import com.example.smartglass.ui.design.CheckUiColorMode
import com.example.smartglass.ui.design.CheckUiColorMode2
import com.example.smartglass.ui.design.customizecompose.CustomButtonCompose
import com.example.smartglass.ui.design.customizecompose.CustomTextCompose
import com.example.smartglass.ui.design.customizecompose.CustomTextFieldCompose
import com.example.smartglass.ui.design.getCustomButtonInstance
import com.example.smartglass.ui.design.getCustomTextFiledInstance
import com.example.smartglass.ui.design.getCustomTextInstance

class SignUpView(private val appCompatActivity: ComponentActivity, private val context: Context) {
    private val textFieldCompose: CustomTextFieldCompose = getCustomTextFiledInstance()
    private val textCompose: CustomTextCompose = getCustomTextInstance()
    private val buttonCompose: CustomButtonCompose = getCustomButtonInstance()


    @Composable
    fun signUpView(navController: NavController, vm: FirebaseViewModel) {
        Surface {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())

            ) {
                LogoAndShapeAndTextIntroductionSection()
                Spacer(modifier = Modifier.height(5.dp))
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 35.dp)
                ) {
                    SignUpTextFieldsAndButton(navController, vm, context)


                }

            }

        }

    }

    @Composable
    fun SignUpTextFieldsAndButton(
        navController: NavController,
        vm: FirebaseViewModel,
        context: Context
    ) {

        val userProfileViewModel: UserProfileViewModel by lazy {
            ViewModelProvider(appCompatActivity)[UserProfileViewModel::class.java]
        }
        var signUpMutableList by remember {
            mutableStateOf<List<UserProfileModel>>(emptyList())
        }

        val signUpLiveData = userProfileViewModel.getUserProfile()
        val observerUserProfileModel = Observer<List<UserProfileModel>> { newData ->
            signUpMutableList = newData
        }
        signUpLiveData.observe(appCompatActivity, observerUserProfileModel)


        var textFullNameValue by remember {
            mutableStateOf("")
        }

        var textUserNameValue by remember {
            mutableStateOf("")
        }

        var textEmailValue by remember {
            mutableStateOf("")
        }

        var textPhoneNumberValue by remember {
            mutableStateOf("")
        }

        var textPasswordValue by remember {
            mutableStateOf("")
        }

        var textConfirmPasswordValue by remember {
            mutableStateOf("")
        }

        val regexName = Regex("^[a-zA-Z].*")
        val regexEmail = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")

        var isFullNameValid by remember { mutableStateOf(true) }
        var isUserNameValid by remember { mutableStateOf(true) }
        var isEmailValid by remember { mutableStateOf(true) }
        var isPhoneNumberValid by remember { mutableStateOf(true) }
        var isPasswordValid by remember { mutableStateOf(true) }
        var isConfirmPasswordMatch by remember { mutableStateOf(true) }

        var errorMessageFullName by remember { mutableStateOf("") }
        var errorMessageUserName by remember { mutableStateOf("") }
        var errorMessageEmail by remember { mutableStateOf("") }
        var errorMessagePhoneNumber by remember { mutableStateOf("") }
        var errorMessagePassword by remember { mutableStateOf("") }
        var errorMessageConfirmPassword by remember { mutableStateOf("") }

        textFieldCompose.copy(shape = 8)
            .CustomizeTextField(

                onValueChanges = {/*TODO("FullNameTextFieldSignUP")*/
                    textFullNameValue = it
                    isFullNameValid = regexName.matches(textFullNameValue)
                    errorMessageFullName = if (!isFullNameValid) "" +
                            "can not start with number or special characters" else ""
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth(),
                label = { textCompose.CustomizeText(text = stringResource(id = R.string.FullName)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = ""
                    )
                },
                isError = !isFullNameValid
            )
        if (errorMessageFullName.isNotEmpty()) {
            textCompose.copy(fontSize = 8, color = Color.Red)
                .CustomizeText(text = errorMessageFullName)
        }

        Spacer(modifier = Modifier.height(8.dp))
        textFieldCompose.copy(shape = 8)
            .CustomizeTextField(
                onValueChanges = {/*TODO("UserNameTextFieldSignUP")*/
                    textUserNameValue = it
                    isUserNameValid = regexName.matches(textUserNameValue)
                    errorMessageUserName = if (!isUserNameValid) "" +
                            "can not start with number or special characters" else ""
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                label = { textCompose.CustomizeText(text = stringResource(id = R.string.UserName)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.AccountBox,
                        contentDescription = ""
                    )
                },
                isError = !isUserNameValid
            )
        if (errorMessageUserName.isNotEmpty()) {
            textCompose.copy(fontSize = 8, color = Color.Red)
                .CustomizeText(text = errorMessageUserName)
        }

        Spacer(modifier = Modifier.height(8.dp))
        textFieldCompose.copy(shape = 8).CustomizeTextField(
            onValueChanges = {/*TODO("EmailTextFieldSignUP")*/
                textEmailValue = it
                isEmailValid = regexEmail.matches(textEmailValue)
                errorMessageEmail = if (!isEmailValid) "Invalid Email" else ""
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(),
            label = { textCompose.CustomizeText(text = stringResource(id = R.string.EmailSign)) },
            leadingIcon = { Icon(imageVector = Icons.Filled.Email, contentDescription = "") },
            isError = !isEmailValid
        )
        if (errorMessageEmail.isNotEmpty()) {
            textCompose.copy(fontSize = 8, color = Color.Red)
                .CustomizeText(text = errorMessageEmail)
        }

        Spacer(modifier = Modifier.height(8.dp))
        textFieldCompose.copy(shape = 8)
            .CustomizeTextField(
                onValueChanges = {/*TODO("PhoneNumberTextFieldSignUP")*/
                    textPhoneNumberValue = it
                    isPhoneNumberValid =
                        textPhoneNumberValue.length == 11 &&
                                (textPhoneNumberValue.startsWith("010")) ||
                                (textPhoneNumberValue.startsWith("011")) ||
                                (textPhoneNumberValue.startsWith("012")) ||
                                (textPhoneNumberValue.startsWith("015"))
                    errorMessagePhoneNumber = if (!isPhoneNumberValid) "Invalid PhoneNumber" else ""

                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                label = { textCompose.CustomizeText(text = stringResource(id = R.string.PhoneNumber)) },
                leadingIcon = { Icon(imageVector = Icons.Filled.Phone, contentDescription = "") },
                isError = !isPhoneNumberValid
            )

        if (errorMessagePhoneNumber.isNotEmpty()) {
            textCompose.copy(fontSize = 8, color = Color.Red).CustomizeText(
                text = errorMessagePhoneNumber,

                )
        }

        Spacer(modifier = Modifier.height(8.dp))
        textFieldCompose.copy(shape = 8)
            .CustomizeTextField(
                onValueChanges = {/*TODO("PasswordTextFieldSignUP")*/
                    textPasswordValue = it
                    isPasswordValid = textPasswordValue.length >= 6
                    errorMessagePassword =
                        if (!isPasswordValid) "invalid password At least 6 character" else ""

                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                label = { textCompose.CustomizeText(text = stringResource(id = R.string.PasswordSignUp)) },
                leadingIcon = { Icon(imageVector = Icons.Filled.Lock, contentDescription = "") },
                isError = !isPasswordValid
            )
        if (errorMessagePassword.isNotEmpty()) {
            textCompose.copy(fontSize = 8, color = Color.Red).CustomizeText(
                text = errorMessagePassword,

                )
        }

        Spacer(modifier = Modifier.height(8.dp))
        textFieldCompose.copy(shape = 8)
            .CustomizeTextField(
                onValueChanges = {/*TODO("ConfirmPasswordTextFieldSignUP")*/
                    textConfirmPasswordValue = it
                    isConfirmPasswordMatch = it == textPasswordValue
                    errorMessageConfirmPassword =
                        if (!isConfirmPasswordMatch) "Password does not match"
                        else ""

                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth(),
                label = { textCompose.CustomizeText(text = stringResource(id = R.string.ConfirmPassword)) },
                leadingIcon = { Icon(imageVector = Icons.Filled.Lock, contentDescription = "") },
                isError = !isConfirmPasswordMatch
            )
        if (errorMessageConfirmPassword.isNotEmpty()) {
            textCompose.copy(fontSize = 8, color = Color.Red).CustomizeText(
                text = errorMessageConfirmPassword,

                )
        }

        Spacer(modifier = Modifier.height(20.dp))
        buttonCompose.copy(shape = 8).CustomizeButton(
            onclick = { /*TODO("SignUPButtonSignUp")*/
                if (textFullNameValue.isNotEmpty() &&
                    textUserNameValue.isNotEmpty() &&
                    textEmailValue.isNotEmpty() &&
                    textPhoneNumberValue.isNotEmpty() &&
                    textPasswordValue.isNotEmpty() &&
                    textConfirmPasswordValue.isNotEmpty() &&
                    textPasswordValue == textConfirmPasswordValue
                ) {
                    val userProfileModel = UserProfileModel(
                        fullName = textFullNameValue,
                        userName = textUserNameValue,
                        email = textEmailValue,
                        phoneNumber = textPhoneNumberValue,
                        password = textPasswordValue,

                        )
                    //
                    vm.onSignup(userProfileModel, textEmailValue, textPasswordValue) { success ->
                        if (success) {
                            vm.fetchUserProfileFromFirebase(textEmailValue)
                            navController.navigate("/login")
                        }
                    }
                    vm.signedIn.value = false
                } else {
                    Toast.makeText(context, "please fill all field ", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled =
            isFullNameValid &&
                    isUserNameValid &&
                    isEmailValid &&
                    isPhoneNumberValid &&
                    isPasswordValid &&
                    isConfirmPasswordMatch
        ) {
            textCompose.copy(fontSize = 18)
                .CustomizeText(
                    text = stringResource(id = R.string.SignUpButton),
                )


        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            buttonCompose.copy(shape = 8)
                .CustomizeTextButton(
                    onclick = { navController.navigate("/login") }
                ) {
                    textCompose.copy(fontSize = 13, color = CheckUiColorMode2())
                        .CustomizeTextButton(
                            text = stringResource(id = R.string.LogIn)
                        )
                }

        }


    }


    @Composable
    fun LogoAndShapeAndTextIntroductionSection() {
        // val uiColor = if (isSystemInDarkTheme()) Color.White else Black
        Box(contentAlignment = Alignment.TopCenter) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fraction = 0.35f),
                painter = painterResource(id = R.drawable.shape),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
            Row(
                modifier = Modifier.padding(top = 80.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(42.dp),
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = stringResource(id = R.string.app_Logo),
                    tint = CheckUiColorMode(),
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    textCompose.copy(
                        fontSize = 25,
                        fontWeight = FontWeight.Bold,

                        )
                        .CustomizeText(text = stringResource(id = R.string.CreateAnAccount))

                }
            }
        }
    }
}