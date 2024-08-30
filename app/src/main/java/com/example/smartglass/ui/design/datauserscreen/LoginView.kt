package com.example.smartglass.ui.design.datauserscreen


import android.content.Context
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
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
import com.example.smartglass.utlity.EmailCache
import com.example.smartglass.utlity.TypeIdUserLogin
import com.example.smartglass.data.model.SignInState
import com.example.smartglass.data.model.UserProfileModel
import com.example.smartglass.data.viewmodel.FirebaseViewModel
import com.example.smartglass.data.viewmodel.UserProfileViewModel
import com.example.smartglass.ui.design.CheckUiColorMode
import com.example.smartglass.ui.design.CheckUiColorMode2
import com.example.smartglass.ui.design.customizecompose.CustomButtonCompose
import com.example.smartglass.ui.design.customizecompose.CustomImageCompose
import com.example.smartglass.ui.design.customizecompose.CustomTextCompose
import com.example.smartglass.ui.design.customizecompose.CustomTextFieldCompose
import com.example.smartglass.ui.design.getCustomButtonInstance
import com.example.smartglass.ui.design.getCustomImageInstance
import com.example.smartglass.ui.design.getCustomTextFiledInstance
import com.example.smartglass.ui.design.getCustomTextInstance
import kotlin.system.exitProcess


class LoginView(private val appCompatActivity: ComponentActivity, val context: Context) {
    private val textFieldCompose: CustomTextFieldCompose = getCustomTextFiledInstance()
    private val textCompose: CustomTextCompose = getCustomTextInstance()
    private val buttonCompose: CustomButtonCompose = getCustomButtonInstance()
    private val imageCompose: CustomImageCompose = getCustomImageInstance()


    @Composable
    fun loginView(
        navController: NavController,
        vm: FirebaseViewModel,
        onSignInClick: () -> Unit,
        state: SignInState
    ) {

        Surface {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),

                ) {
                LogoAndShapeAndTextIntroductionSection()
                Spacer(modifier = Modifier.height(15.dp))
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 30.dp)
                ) {
                    LoginSection(navController, vm)
                    Spacer(modifier = Modifier.height(8.dp))
                    LoginWithSocialMediaSection(onSignInClick, state)
                    JoinNowSection(navController)

                }

            }

        }
        YourDestinationScreen(navController = navController)

    }


    @Composable
    private fun JoinNowSection(navController: NavController) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,

            ) {
            buttonCompose.CustomizeTextButton(onclick = { navController.navigate("/signup") }) {
                textCompose.copy(fontSize = 13, color = CheckUiColorMode2())
                    .CustomizeTextButton(text = stringResource(id = R.string.JoinNow))
            }

        }
    }

    @Composable
    fun LoginWithSocialMediaSection(onSignInClick: () -> Unit, state: SignInState) {
        LaunchedEffect(key1 = state.signInError) {
            state.signInError?.let { error ->
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }

        }


        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            textCompose.copy(fontSize = 12).CustomizeText(
                text = stringResource(id = R.string.OrContinueWith)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically

            ) {
                imageCompose.copy(horizontalArrangement = Arrangement.Center).CustomizeImage(
                    icon = R.drawable.google,
                    text = stringResource(id = R.string.GoogleIcon),
                    modifier = Modifier.weight(1f),
                    onClick = {/*TODO("Google Login Button")*/
                        onSignInClick()
                    },
                )
            }

        }
    }

    @Composable
    fun LoginSection(navController: NavController, vm: FirebaseViewModel) {
        val view = LocalView.current

        val userProfileViewModel: UserProfileViewModel by lazy {
            ViewModelProvider(appCompatActivity)[UserProfileViewModel::class.java]
        }
        var loginMutableList by remember {
            mutableStateOf<List<UserProfileModel>>(emptyList())
        }
        val loginLiveData = userProfileViewModel.getUserProfile()
        val observerUserProfileModel = Observer<List<UserProfileModel>> { newData ->
            loginMutableList = newData
        }
        loginLiveData.observe(appCompatActivity, observerUserProfileModel)


        var textEmailLoginValue by remember {
            mutableStateOf("")
        }
        var passwordLoginValue by remember {
            mutableStateOf("")
        }
        var ipTextValue by remember {
            mutableStateOf("")
        }

        textFieldCompose.copy(shape = 8, contentDescriptions = "EmailTextField")
            .CustomizeTextField(onValueChanges = {
                /*TODO("Email Text Filed Login")*/ textEmailLoginValue = it
            },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
                ),
                label = {
                    textCompose.copy().CustomizeText(
                        text = stringResource(id = R.string.EmailLogin)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.AccountBox,
                        contentDescription = null
                    )
                })

        Spacer(modifier = Modifier.height(20.dp))

        textFieldCompose.copy(shape = 8, contentDescriptions = "PasswordTextField")
            .CustomizeTextField(onValueChanges = {
                /*TODO("Password Text Field Login")*/
                passwordLoginValue = it
            },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                ),
                label = {
                    textCompose.copy().CustomizeText(
                        text = stringResource(id = R.string.PasswordLogin)
                    )
                },
                leadingIcon = { Icon(imageVector = Icons.Filled.Lock, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    TextButton(onClick = {/*TODO("ForgetPassword Button")*/
                        navController.navigate("/forgetPasswordView")
                    }) {
                        textCompose.copy(color = CheckUiColorMode2())
                            .CustomizeTextButton(text = stringResource(id = R.string.ForgetPassword))

                    }
                })

        Spacer(modifier = Modifier.height(20.dp))

        vm.getTypeIdUserLogin(textEmailLoginValue)
        buttonCompose.copy(shape = 8, contentDescriptions = "LoginButton").CustomizeButton(
            modifier = Modifier.fillMaxWidth(),
            onclick = {/*TODO("LoginTransferToSignUpView")*/
                EmailCache.emailCache = textEmailLoginValue

                if (vm.isRestPassword.value == true) {
                    vm.updatedPasswordInDatabase(textEmailLoginValue, passwordLoginValue)

                }

                if (loginMutableList.isEmpty() && TypeIdUserLogin.typeIdUserLogin == 0) {
                    vm.fetchUserProfileFromFirebase(textEmailLoginValue)

                }
                if (textEmailLoginValue.isNotEmpty() && passwordLoginValue.isNotEmpty()) {
                    if (TypeIdUserLogin.typeIdUserLogin == 0) {
                        vm.login(textEmailLoginValue, passwordLoginValue, context) { success ->
                            if (success) {
                                vm.isRestPassword.value = false
                                vm.acceptedNotificationConnected()
                                //IpChange.value = ipTextValue
                                navController.navigate("/home") {

                                }

                            }

                        }
                    } else {
                        Toast.makeText(
                            context, "This account dose not include in app",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    Toast.makeText(
                        context, " Email or password value is Empty", Toast.LENGTH_SHORT
                    ).show()
                }


            },
        ) {
            textCompose.copy(fontSize = 18).CustomizeText(
                text = stringResource(R.string.LoginButton)

            )


        }

    }


    @Composable
    fun LogoAndShapeAndTextIntroductionSection() {
        // val uiColor = if (isSystemInDarkTheme()) Color.White else Black
        Box(contentAlignment = Alignment.TopCenter) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fraction = 0.46f),
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
                        fontSize = 25, fontWeight = FontWeight.Bold
                    ).CustomizeText(text = stringResource(id = R.string.welcomeTextLogin))
                    textCompose.copy(
                        fontSize = 20, fontWeight = FontWeight.Bold
                    ).CustomizeText(text = stringResource(id = R.string.textLogin))
                }
            }
        }
    }

    @Composable
    fun YourDestinationScreen(navController: NavController) {
        BackHandler {
            navController.popBackStack()
            exitApp()
        }
        DisposableEffect(key1 = Unit) {
            onDispose {
            }
        }
    }

    private fun exitApp() {
        exitProcess(0)
    }

}

