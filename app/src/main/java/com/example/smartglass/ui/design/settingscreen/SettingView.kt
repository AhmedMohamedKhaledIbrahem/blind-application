package com.example.smartglass.ui.design.settingscreen

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.smartglass.data.FirebaseRepositoryImp
import com.example.smartglass.R
import com.example.smartglass.auth.GoogleAuthUiClient
import com.example.smartglass.utlity.EmailCache
import com.example.smartglass.utlity.PersonEmailCache
import com.example.smartglass.utlity.NotificationHashCode
import com.example.smartglass.utlity.TypeIdUserLogin
import com.example.smartglass.utlity.UriCache
import com.example.smartglass.data.model.ConnectedDeviceContactModel
import com.example.smartglass.data.model.UserDataFromGoogleAccount
import com.example.smartglass.data.model.UserProfileModel
import com.example.smartglass.data.viewmodel.ConnectedDeviceContactViewModel
import com.example.smartglass.data.viewmodel.EmergencyContactViewModel
import com.example.smartglass.data.viewmodel.FireStorageViewModel
import com.example.smartglass.data.viewmodel.FirebaseViewModel
import com.example.smartglass.data.viewmodel.UserProfileViewModel
import com.example.smartglass.service.VoiceRecognitionService
import com.example.smartglass.theme.skyBlue
import com.example.smartglass.ui.design.customizecompose.CustomImageCompose
import com.example.smartglass.ui.design.customizecompose.CustomTextCompose
import com.example.smartglass.ui.design.getCustomImageInstance
import com.example.smartglass.ui.design.getCustomTextInstance
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class SettingView(
    private val context: Context,
    private val appCompatActivity: ComponentActivity,
    private val application: Application
) {
    private val textCompose: CustomTextCompose = getCustomTextInstance()
    private val imageCompose: CustomImageCompose = getCustomImageInstance()
    private var parse: String? = null
    private var serviceIntent: Intent? = null
    var fireStorageFile = FireStorageViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun settingView(
        navController: NavController, activity: ComponentActivity, vm: FirebaseViewModel,
        googleAuthUiClient: GoogleAuthUiClient,
        userDataFromGoogleAccount: UserDataFromGoogleAccount?,


        ) {

        LaunchedEffect(Unit) {
            serviceIntent = Intent(context, VoiceRecognitionService::class.java)
            context.stopService(serviceIntent)
        }
        YourDestinationScreen(navController, activity)

        Surface {
            Column(
                Modifier
                    .fillMaxSize()
            ) {
                LogoAndShapeAndTextIntroductionSection()
                Spacer(modifier = Modifier.height(40.dp))
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 2.dp)

                ) {
                    ImageProfileAndFullNameSection(
                        userDataFromGoogleAccount,
                        vm,
                        googleAuthUiClient,
                    )


                    Spacer(modifier = Modifier.height(120.dp))
                    UserSettingSection(navController, vm, googleAuthUiClient)
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
                        text = stringResource(id = R.string.Setting),
                        modifier = Modifier.padding(vertical = 80.dp),
                    )
            }


        }
    }


    @Composable
    fun ImageProfileAndFullNameSection(
        userDataFromGoogleAccount: UserDataFromGoogleAccount?,
        vm: FirebaseViewModel,
        googleAuthUiClient: GoogleAuthUiClient

    ) {
        val userProfileViewModel: UserProfileViewModel by lazy {
            ViewModelProvider(appCompatActivity)[UserProfileViewModel::class.java]
        }

        var userProfileModelMutable by remember {
            mutableStateOf(value = UserProfileModel())
        }

        val userProfileLiveData = userProfileViewModel.getUserProfileSingleData()
        val observerUserProfileModel = Observer<UserProfileModel?> { newData ->
            if (newData != null) {
                userProfileModelMutable = newData
            }
        }
        userProfileLiveData.observe(appCompatActivity, observerUserProfileModel)

        val googleAccount = googleAuthUiClient.getSignedInUser()?.email
        val googleID = googleAuthUiClient.getSignedInUser()?.userId

        if (googleAccount != null) {
            if (googleID != null) {
                vm.getImageFromGoogleEmail(googleAccount, googleID)
            }
        }


        var selectedImage by remember {
            mutableStateOf(userProfileModelMutable.uri.toUri())
        }

        val getImageGoogle by rememberUpdatedState(newValue = UriCache.uri.toUri())
        val pickPhoto =
            rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    /*
                    userProfileModelMutable.uri = parse ?: ""
                    selectedImage = userProfileModelMutable.uri.toUri()
                    userProfileViewModel.insertUserProfile(userProfileModelMutable)
                    vm.uploadImage(EmailCache.emailCache, selectedImage.toString())*/
                    parse = getImagePathFromUri(context, uri)
                    userProfileModelMutable.uri = parse.toString()
                    selectedImage = userProfileModelMutable.uri.toUri()
                    uploadImageToFirebase(selectedImage,userProfileViewModel,userProfileModelMutable)
                }

            }




        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.height(100.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = skyBlue
                ),
            ) {
                Spacer(modifier = Modifier.height(25.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(32.dp))
                    val shape = RoundedCornerShape(
                        topStart = 50.dp,
                        topEnd = 50.dp,
                        bottomStart = 50.dp,
                        bottomEnd = 50.dp
                    )
                    Log.i("userProfileTest", "$selectedImage")
                    if (userProfileModelMutable.uri.isNotEmpty()){
                    AsyncImage(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(shape = shape)
                            .clickable { pickPhoto.launch("image/*") },
                        model = userProfileModelMutable.uri,
                        contentDescription = "Image Profile",
                    )}else {
                        Image(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(shape = shape)
                                .clickable { pickPhoto.launch("image/*") },
                            painter = painterResource(id = R.drawable.account_profile),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                    if (userProfileModelMutable.fullName.isNotEmpty()) {
                        textCompose.copy(
                            fontSize = 18,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ).CustomizeTextImage(text = userProfileModelMutable.fullName)
                    }

                }
            }
        }
    }

    @Composable
    fun UserSettingSection(
        navController: NavController,
        vm: FirebaseViewModel,
        googleAuthUiClient: GoogleAuthUiClient
    ) {
        val firebaseRepositoryImp = FirebaseRepositoryImp(application)
        val logoutViewModel: UserProfileViewModel by lazy {
            ViewModelProvider(appCompatActivity)[UserProfileViewModel::class.java]
        }
        val deleteContactWhenLogOut: EmergencyContactViewModel by lazy {
            ViewModelProvider(appCompatActivity)[EmergencyContactViewModel::class.java]
        }
        var checkValue by remember {
            mutableStateOf(true)
        }

        val connectedDeviceContactViewModel: ConnectedDeviceContactViewModel by lazy {
            ViewModelProvider(appCompatActivity)[ConnectedDeviceContactViewModel::class.java]
        }
        var connectedDeviceContactMutableList by remember {
            mutableStateOf<List<ConnectedDeviceContactModel>>(emptyList())
        }
        val connectedDeviceContactLiveData =
            connectedDeviceContactViewModel.getConnectedDeviceContact()

        val observerConnectedDeviceContactModel =
            Observer<List<ConnectedDeviceContactModel>> { newData ->
                connectedDeviceContactMutableList = newData
            }
        connectedDeviceContactLiveData.observe(
            appCompatActivity,
            observerConnectedDeviceContactModel
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            imageCompose.copy(
                modifierSizeImage = 80,
                fontSizeTextImage = 15,
                modifierWidthSpacerSwitch = 150,
                fontWeightTextImage = FontWeight.Bold
            ).CustomizeImageWithoutOnClick(
                icon = R.drawable.parentalcontrollogo,
                text = stringResource(id = R.string.ParentalControl),
            ) {/*TODO("ParentalControlSwitch")*/checkValue = it
                if (checkValue) {

                    vm.notificationShow(context)

                } else {

                    ///val notificationManager =
                   // context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                   // notificationManager.cancelAll()
                }
            }

        }



        Spacer(modifier = Modifier.height(8.dp))
        imageCompose.copy(
            modifierSizeImage = 80,
            fontSizeTextImage = 15,
            fontWeightTextImage = FontWeight.Bold,
        ).CustomizeImage(
            icon = R.drawable.emergencylogo,
            text = stringResource(id = R.string.EmergencyLogo)
        ) {
            navController.navigate("/emergencyPlanView")

        }
        Spacer(modifier = Modifier.height(8.dp))
        imageCompose.copy(
            modifierSizeImage = 80,
            fontSizeTextImage = 15,
            fontWeightTextImage = FontWeight.Bold,
        ).CustomizeImage(
            icon = R.drawable.logoutlogo,
            text = stringResource(id = R.string.LogoutLogo)
        ) {

            googleAuthUiClient.signOut()
            logoutViewModel.deleteAll()
            deleteContactWhenLogOut.deleteAllContact()
            connectedDeviceContactViewModel.deleteAllConnectedDeviceContact()
            vm.signOut()
            EmailCache.emailCache = ""
            PersonEmailCache.value = ""
            TypeIdUserLogin.typeIdUserLogin = null
            NotificationHashCode.value=0
            navController.navigate("/login")

        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun YourDestinationScreen(navController: NavController, activity: ComponentActivity) {
        // Your screen UI code goes here

        // Handle back button press
        BackHandler {
            // Navigate back when back button is pressed
            navController.popBackStack()

            activity.startService(Intent(activity, VoiceRecognitionService::class.java))

        }

        // Stop the service when leaving the screen
        DisposableEffect(key1 = Unit) {
            // Clean-up logic goes here

            //  activity.stopService(Intent(activity, VoiceRecognitionService::class.java))


            onDispose {
                // Dispose logic goes here if needed
            }
        }
    }

    private fun getImagePathFromUri(context: Context, uri: Uri): String? {
        var path: String? = null
        //val context = context
        val contentResolver: ContentResolver = context.contentResolver
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            path = it.getString(columnIndex)
        }
        cursor?.close()
        return path
    }

    private fun uploadImageToFirebase(uri: Uri, userProfileViewModel: UserProfileViewModel ,userProfileModelMutable:UserProfileModel) {
        val photoRef = FirebaseStorage.getInstance().getReference("photos/photo1.jpg")

        // Verify the URI before attempting to putFile
        Log.d("UploadImageToFirebase", "Uploading image from URI: $uri")

        try {
            // Convert Uri to File
            val file = File(uri.path!!)

            // Proceed with putFile operation
            val uploadTask = photoRef.putFile(Uri.fromFile(file))

            uploadTask.addOnSuccessListener { uploadTaskSnapshot ->
                Log.d("UploadImageToFirebase", "Photo uploaded successfully")

                // Once the photo is uploaded, get its download URL
                photoRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    val photoUrl = downloadUri.toString()
                    Log.d("DownloadURL", "Photo download URL: $photoUrl")

                    // Update user profile with the image URL
                        val test = userProfileModelMutable
                    test.uri = photoUrl
                    userProfileViewModel.insertUserProfile(test)
                }.addOnFailureListener { downloadException ->
                    Log.e("DownloadURL", "Error getting download URL", downloadException)
                }
            }.addOnFailureListener { uploadException ->
                Log.e("UploadImageToFirebase", "Error uploading photo", uploadException)
            }
        } catch (e: Exception) {
            Log.e("UploadImageToFirebase", "Error: ${e.message}", e)
        }
    }



}




