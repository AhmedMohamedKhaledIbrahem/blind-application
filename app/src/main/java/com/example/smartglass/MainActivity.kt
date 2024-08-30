package com.example.smartglass

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.smartglass.auth.GoogleAuthUiClient
import com.example.smartglass.data.FirebaseRepositoryImp
import com.example.smartglass.utlity.EmailCache
import com.example.smartglass.data.viewmodel.FirebaseViewModel
import com.example.smartglass.data.viewmodel.KeyDownEventViewModel
import com.example.smartglass.data.viewmodel.NetworkConnectivityViewModel
import com.example.smartglass.data.viewmodel.SignInWithGoogleStateViewModel
import com.example.smartglass.service.NotificationBroadcastReceiver
import com.example.smartglass.service.ShortMessageService
import com.example.smartglass.service.VoiceRecognitionService
import com.example.smartglass.theme.SmartGlassTheme
import com.example.smartglass.ui.design.emergencyscreen.EmergencyPlanContactView
import com.example.smartglass.ui.design.emergencyscreen.EmergencyPlanView
import com.example.smartglass.ui.design.datauserscreen.ForgetPasswordView
import com.example.smartglass.ui.design.homescreen.HomeView
import com.example.smartglass.ui.design.datauserscreen.LoginView
import com.example.smartglass.ui.design.datauserscreen.PasswordChangeView
import com.example.smartglass.ui.design.settingscreen.SettingView
import com.example.smartglass.ui.design.datauserscreen.SignUpView
import com.example.smartglass.utlity.ActivityUtils
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // private val networkConnectivityViewModel: NetworkConnectivityViewModel by viewModels()
    private lateinit var networkConnectivityViewModel: NetworkConnectivityViewModel
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    private var tts: TextToSpeech? = null
    private val receiver = NotificationBroadcastReceiver()

    var keyDownEventViewModel = KeyDownEventViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val loginView = LoginView(this, this)
        val signUpView = SignUpView(this, this)
        val settingView = SettingView(this, this, application)
        val emergencyPlanView = EmergencyPlanView(this)
        val emergencyPlanContactView = EmergencyPlanContactView(this)
        val homeView = HomeView(applicationContext, this@MainActivity)
        val forgetPasswordView = ForgetPasswordView(this)
        val passwordChangeView = PasswordChangeView()
        val shortMessageService = ShortMessageService(this)
        networkConnectivityViewModel = NetworkConnectivityViewModel(this)

        ActivityUtils.acitvityComponant = this
        ActivityUtils.activityContext = applicationContext
        ActivityUtils.homeView = homeView
        ActivityUtils.appCompatActivity = this
        FirebaseRepositoryImp(application).messageSender()

        tts = TextToSpeech(this)
        { status ->
            if (status != TextToSpeech.ERROR) {
                tts?.language = Locale.getDefault()
            }
        }

        if (!hasRequiredPermissionsCamera()) {
            ActivityCompat.requestPermissions(
                this, CAMERA_PERMISSIONS, 0
            )
        }

        /*networkConnectivityViewModel.observe(this) { isConnected ->
            if (isConnected) {

            } else {
                tts?.speak(
                    "lost connection the emergency plan activated ",
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                )
                shortMessageService.smsEmergencyContact()
            }
        }*/


        val filter = IntentFilter("ACCEPT_ACTION")
        registerReceiver(receiver, filter, RECEIVER_NOT_EXPORTED)

        val filter2 = IntentFilter("Ok_ACTION")
        registerReceiver(receiver, filter2, RECEIVER_NOT_EXPORTED)

        setContent {
            SmartGlassTheme {
                val state = networkConnectivityViewModel.observeAsState().value
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination?.route
                Log.e("current",currentDestination.toString())
                if (state == false) {
                    tts?.speak(
                        "lost connection ",
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                    )
                    NetworkStatusScreen(state = state)
                    stopVoiceRecognitionService()
                    if (currentDestination=="/login"||currentDestination=="/signup"){
                        tts?.speak(
                            "lost connection yyy ",
                            TextToSpeech.QUEUE_FLUSH,
                            null,
                        )
                    }else{
                        shortMessageService.smsEmergencyContact()
                    }
                } else {

                    // Define the navigation graph
                    NavigationControllerLoginViewToSignUpView(
                        navController,
                        loginView,
                        signUpView,
                        homeView,
                        settingView,
                        emergencyPlanView,
                        emergencyPlanContactView,
                        forgetPasswordView,
                        passwordChangeView,
                    )
                }

            }


        }


    }


    override fun onStop() {
        super.onStop()
        stopVoiceRecognitionService()
    }

    override fun onDestroy() {
        super.onDestroy()
        tts?.stop()
        tts?.shutdown()
        stopVoiceRecognitionService()
        unregisterReceiver(receiver)
    }

    private fun stopVoiceRecognitionService() {
        val serviceIntent = Intent(this, VoiceRecognitionService::class.java)
        stopService(serviceIntent)
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun NetworkStatusScreen(state: Boolean?) {

        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) {

            if (state == false) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(50.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator()
                }

            }


        }
    }


    @SuppressLint("RememberReturnType")
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    private fun NavigationControllerLoginViewToSignUpView(
        navController: NavHostController,
        loginView: LoginView,
        signUpView: SignUpView,
        homeView: HomeView,
        settingView: SettingView,
        emergencyPlanView: EmergencyPlanView,
        emergencyPlanContactView: EmergencyPlanContactView,
        forgetPasswordView: ForgetPasswordView,
        passwordChangeView: PasswordChangeView,
    ) {


        val viewModelFireBase = hiltViewModel<FirebaseViewModel>()

        LaunchedEffect(Unit) {
            if (viewModelFireBase.isSignedIn()) {
                navController.navigate("/home")
            }
        }


        NavHost(navController = navController, startDestination = "/login") {
            composable("/login") {
                val viewModel = viewModel<SignInWithGoogleStateViewModel>()
                val state by viewModel.state.collectAsState()
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if (result.resultCode == RESULT_OK) {
                            lifecycleScope.launch {
                                val signInResult = googleAuthUiClient.signInWithIntent(
                                    intent = result.data ?: return@launch
                                )
                                viewModel.onSignInResult(signInResult)

                            }
                        }
                    }
                )

                LaunchedEffect(key1 = state) {

                    if (state.isSignInSuccessful) {
                        googleAuthUiClient.getSignedInUser()?.username?.let { it1 ->
                            viewModelFireBase.uploadEmailGoogleToDatabase(
                                googleAuthUiClient.getSignedInUser()!!.email,
                                it1,
                                googleAuthUiClient.getSignedInUser()!!.userId
                            )

                        }
                        val getIdType = viewModelFireBase.getIdTypeUserGoogleAccount(
                            googleAuthUiClient.getSignedInUser()!!.email,
                            googleAuthUiClient.getSignedInUser()!!.userId
                        )
                        if (getIdType == 0 || getIdType == null) {

                            viewModelFireBase.fetchUserProfileFromFirebase(
                                googleAuthUiClient.getSignedInUser()!!.email
                            )
                            viewModelFireBase.acceptedNotificationConnected()
                            EmailCache.emailCache = googleAuthUiClient.getSignedInUser()!!.email
                            Toast.makeText(
                                applicationContext,
                                "Sign In successful",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.navigate("/home")
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Sign In Unsuccessful",
                                Toast.LENGTH_SHORT
                            ).show()
                            googleAuthUiClient.signOut()
                            viewModelFireBase.signOut()
                            navController.navigate("/login")
                        }

                    }


                }
                loginView.loginView(navController = navController, viewModelFireBase, state = state,
                    onSignInClick = {
                        lifecycleScope.launch {
                            val signINIntentSender = googleAuthUiClient.signIn()

                            launcher.launch(
                                IntentSenderRequest.Builder(
                                    signINIntentSender ?: return@launch
                                ).build()
                            )
                        }
                    })


            }
            composable("/signup") {
                signUpView.signUpView(navController = navController, viewModelFireBase)
            }
            composable(
                "/home",
                arguments = listOf(navArgument("popUpTo") { defaultValue = "/login" })
            ) {
                homeView.BottomSheetScaffoldState(
                    navController = navController,
                    this@MainActivity,
                    googleAuthUiClient,
                    viewModelFireBase
                )

            }
            composable("/setting") {

                settingView.settingView(
                    navController = navController, this@MainActivity, viewModelFireBase,
                    googleAuthUiClient,
                    googleAuthUiClient.getSignedInUser(),
                )
            }
            composable("/emergencyPlanView") {
                emergencyPlanView.emergencyView(navController = navController)
            }
            composable("/emergencyPlanContactView") {
                emergencyPlanContactView.emergencyPlanContactView(
                    navController = navController,
                    viewModelFireBase
                )
            }
            composable("/forgetPasswordView") {
                forgetPasswordView.forgetPasswordView(
                    navController = navController,
                    viewModelFireBase
                )
            }
            composable("/passwordChange") {
                passwordChangeView.passwordChangeView(navController = navController)
            }


        }


    }


    private fun hasRequiredPermissionsCamera(): Boolean {
        return CAMERA_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                this,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }

    }


    companion object {
        private val CAMERA_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.ACCESS_NOTIFICATION_POLICY,
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.VIBRATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            // Volume down key pressed
            Log.d("MainActivity", "Volume Down key pressed")
            // Perform your action here
            KeyDownEventViewModel.updateData("true")
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

}


