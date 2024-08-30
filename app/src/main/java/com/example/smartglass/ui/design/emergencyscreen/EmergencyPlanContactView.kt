package com.example.smartglass.ui.design.emergencyscreen

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import com.example.smartglass.R
import com.example.smartglass.utlity.EmailCache
import com.example.smartglass.data.model.EmergencyContactModel
import com.example.smartglass.data.viewmodel.EmergencyContactViewModel
import com.example.smartglass.data.viewmodel.FirebaseViewModel
import com.example.smartglass.ui.design.customizecompose.CustomButtonCompose
import com.example.smartglass.ui.design.customizecompose.CustomImageCompose
import com.example.smartglass.ui.design.customizecompose.CustomTextCompose
import com.example.smartglass.ui.design.customizecompose.CustomTextFieldCompose
import com.example.smartglass.ui.design.getCustomButtonInstance
import com.example.smartglass.ui.design.getCustomImageInstance
import com.example.smartglass.ui.design.getCustomTextFiledInstance
import com.example.smartglass.ui.design.getCustomTextInstance

class EmergencyPlanContactView(private val appCompatActivity: ComponentActivity) {
    private val textFieldCompose: CustomTextFieldCompose = getCustomTextFiledInstance()
    private val textCompose: CustomTextCompose = getCustomTextInstance()
    private val buttonCompose: CustomButtonCompose = getCustomButtonInstance()
    private val imageCompose: CustomImageCompose = getCustomImageInstance()


    @Composable
    fun emergencyPlanContactView(navController: NavHostController, vm: FirebaseViewModel) {

        Surface() {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                LogoAndShapeAndTextIntroductionSection()
                Spacer(modifier = Modifier.height(15.dp))
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 30.dp)

                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    AddContactAndButtonSection(navController, vm)


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
                    color = Color.Black
                )
                    .CustomizeText(
                        text = stringResource(id = R.string.EmergencyPlanContact),
                        modifier = Modifier.padding(vertical = 80.dp),
                    )
            }


        }
    }

    @Composable
    fun AddContactAndButtonSection(navController: NavHostController, vm: FirebaseViewModel) {
        val regexName = Regex("^[a-zA-Z].*")
        var textFullNameValue by remember {
            mutableStateOf("")
        }
        var textPhoneNumberValue by remember {
            mutableStateOf("")
        }
        val emergencyContactViewModel: EmergencyContactViewModel by lazy {
            ViewModelProvider(appCompatActivity)[EmergencyContactViewModel::class.java]
        }
        var emergencyContactMutableList by remember {
            mutableStateOf<List<EmergencyContactModel>>(emptyList())
        }
        var isFullNameValid by remember { mutableStateOf(true) }
        var isPhoneNumberValid by remember { mutableStateOf(true) }
        var errorMessageFullName by remember { mutableStateOf("") }
        var errorMessagePhoneNumber by remember { mutableStateOf("") }
        LaunchedEffect(Unit) {
            val emergencyContactLiveData = emergencyContactViewModel.getEmergencyContact()
            val observerEmergencyContactModel = Observer<List<EmergencyContactModel>> { newData ->
                emergencyContactMutableList = newData
            }
            emergencyContactLiveData.observe(appCompatActivity, observerEmergencyContactModel)

        }

        textFieldCompose.copy(shape = 8, contentDescriptions = "FullNameTextField")
            .CustomizeTextField(
                onValueChanges = {/*TODO("FullNameContact")*/
                    textFullNameValue = it
                    isFullNameValid = regexName.matches(textFullNameValue)
                    errorMessageFullName = if (!isFullNameValid) "" +
                            "can not start with number or special characters" else ""
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                ),
                label = {
                    textCompose.copy(contentDescriptions = "FullNameText").CustomizeText(
                        text = stringResource(id = R.string.FullName)
                    )
                },
                leadingIcon = { Icon(imageVector = Icons.Filled.Email, contentDescription = "") },
                modifier = Modifier.fillMaxWidth(),
                isError = !isFullNameValid
            )

        if (errorMessageFullName.isNotEmpty()) {
            textCompose.copy(fontSize = 8, color = Color.Red)
                .CustomizeText(text = errorMessageFullName)
        }

        Spacer(modifier = Modifier.height(20.dp))

        textFieldCompose.copy(shape = 8, contentDescriptions = "PhoneNumberTextField")
            .CustomizeTextField(
                onValueChanges = {/*TODO("PhoneNumberContact")*/
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
                    imeAction = ImeAction.Done
                ),
                label = {
                    textCompose.copy(contentDescriptions = "PhoneNumberText").CustomizeText(
                        text = stringResource(id = R.string.PhoneNumber)
                    )
                },
                leadingIcon = { Icon(imageVector = Icons.Filled.Lock, contentDescription = "") },
                modifier = Modifier.fillMaxWidth(),
                isError = !isPhoneNumberValid
            )
        if (errorMessagePhoneNumber.isNotEmpty()) {
            textCompose.copy(fontSize = 8, color = Color.Red).CustomizeText(
                text = errorMessagePhoneNumber,

                )
        }

        Spacer(modifier = Modifier.height(20.dp))

        buttonCompose.copy(shape = 8, contentDescriptions = "AddButton").CustomizeButton(
            modifier = Modifier.fillMaxWidth(),
            onclick = {/*TODO("ADDContactButton")*/
                if (textFullNameValue.isNotEmpty() && textPhoneNumberValue.isNotEmpty()) {
                    val emergencyContactModel = EmergencyContactModel(
                        fullName = textFullNameValue,
                        phoneNumber = textPhoneNumberValue,
                    )
                    emergencyContactViewModel.insertEmergencyContact(emergencyContactModel)

                    val email = EmailCache.emailCache
                    vm.uploadContacts(email, listOf(emergencyContactModel))
                    navController.navigate("/emergencyPlanView")
                }
            },
            enabled = isFullNameValid && isPhoneNumberValid
        ) {
            textCompose.copy(fontSize = 18).CustomizeText(
                text = stringResource(R.string.AddContact)
            )
        }
    }


}


