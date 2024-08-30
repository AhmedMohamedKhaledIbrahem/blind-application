package com.example.smartglass.ui.design.emergencyscreen

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.smartglass.R
import com.example.smartglass.data.model.EmergencyContactModel
import com.example.smartglass.data.viewmodel.EmergencyContactViewModel
import com.example.smartglass.theme.skyBlue
import com.example.smartglass.ui.design.customizecompose.CustomButtonCompose
import com.example.smartglass.ui.design.customizecompose.CustomImageCompose
import com.example.smartglass.ui.design.customizecompose.CustomTextCompose
import com.example.smartglass.ui.design.customizecompose.CustomTextFieldCompose
import com.example.smartglass.ui.design.getCustomButtonInstance
import com.example.smartglass.ui.design.getCustomImageInstance
import com.example.smartglass.ui.design.getCustomTextFiledInstance
import com.example.smartglass.ui.design.getCustomTextInstance

class EmergencyPlanView(private val appCompatActivity: ComponentActivity) {
    private val textFieldCompose: CustomTextFieldCompose = getCustomTextFiledInstance()
    private val textCompose: CustomTextCompose = getCustomTextInstance()
    private val buttonCompose: CustomButtonCompose = getCustomButtonInstance()
    private val imageCompose: CustomImageCompose = getCustomImageInstance()


    @Composable
    fun emergencyView(navController: NavHostController) {

        val emergencyContactViewModel: EmergencyContactViewModel by lazy {
            ViewModelProvider(appCompatActivity)[EmergencyContactViewModel::class.java]
        }
        var emergencyContactMutableList by remember {
            mutableStateOf<List<EmergencyContactModel>>(emptyList())
        }

        val emergencyContactLiveData = emergencyContactViewModel.getEmergencyContact()
        val observerEmergencyContactModel = Observer<List<EmergencyContactModel>> { newData ->
            emergencyContactMutableList = newData
        }
        emergencyContactLiveData.observe(appCompatActivity, observerEmergencyContactModel)



        Surface() {
            Column(
                Modifier
                    .fillMaxSize()
            ) {
                LogoAndShapeAndTextIntroductionSection()
                Spacer(modifier = Modifier.height(15.dp))
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 30.dp),

                    ) {
                    EmergencyContactSection(emergencyContactModel = emergencyContactMutableList)
                    //Spacer(modifier = Modifier.height(8.dp))
                    EmergencyFloatingActionButtonSection(navController)


                }

            }

        }
        YourDestinationScreen(navController = navController)

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
                        text = stringResource(id = R.string.EmergencyPlan),
                        modifier = Modifier.padding(vertical = 80.dp),
                    )
            }


        }
    }

    private val itemContact = listOf(
        EmergencyContact(contactFullName = "AhmedKhaled", contactPhoneNumber = "01068288311"),
        EmergencyContact(contactFullName = "MohamedKhaled", contactPhoneNumber = "01068288313")

    )

    @Composable
    fun EmergencyContactSection(emergencyContactModel: List<EmergencyContactModel>) {
        LazyColumn() {

            items(emergencyContactModel) { emergencyContact ->

                ListItem(emergencyContact = emergencyContact)
                Spacer(modifier = Modifier.height(12.dp))
            }

        }

    }

    @Composable
    fun ListItem(emergencyContact: EmergencyContactModel) {


        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.emergency_contact),
                contentDescription = "Icon Contact",
                modifier = Modifier.size(50.dp)
            )
            Column(modifier = Modifier.padding(horizontal = 15.dp)) {

                textCompose.copy(fontSize = 20, fontWeight = FontWeight.Bold)
                    .CustomizeText(text = emergencyContact.fullName)

                textCompose.copy(fontSize = 20, fontWeight = FontWeight.Bold)
                    .CustomizeText(text = emergencyContact.phoneNumber)
            }

        }


    }

    @Composable
    fun EmergencyFloatingActionButtonSection(navController: NavHostController) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(bottom = 30.dp)
        ) {
            FloatingActionButton(
                onClick = { /*TODO("EmergencyFloatingActionButton")*/ navController.navigate("/emergencyPlanContactView") },
                shape = FloatingActionButtonDefaults.largeShape,
                containerColor = skyBlue,
                contentColor = Color.White,
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_add_24),
                    contentDescription = "EmergencyPlanContactFloatingActionButton "
                )
            }

        }
    }

    @Composable
    fun YourDestinationScreen(navController: NavController) {
        BackHandler {
            navController.navigate("/setting")
        }
        DisposableEffect(key1 = Unit) {
            onDispose {
            }
        }
    }

}