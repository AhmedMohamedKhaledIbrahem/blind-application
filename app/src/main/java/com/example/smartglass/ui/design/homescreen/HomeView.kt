package com.example.smartglass.ui.design.homescreen


import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.smartglass.data.GeminiApiClient
import com.example.smartglass.R
import com.example.smartglass.auth.GoogleAuthUiClient
import com.example.smartglass.data.interfaceblind.SmartGlassApi
import com.example.smartglass.data.UploadResponse
import com.example.smartglass.utlity.ObjectDetectFinderList
import com.example.smartglass.data.viewmodel.FireStorageViewModel
import com.example.smartglass.data.viewmodel.FirebaseViewModel
import com.example.smartglass.data.viewmodel.GeminiViewModel
import com.example.smartglass.data.viewmodel.KeyDownEventViewModel
import com.example.smartglass.data.viewmodel.TakePhotoFirebaseViewModel
import com.example.smartglass.data.viewmodel.ViewModelPhoto
import com.example.smartglass.data.viewmodel.VoiceCommandViewModel
import com.example.smartglass.service.VoiceRecognitionService
import com.example.smartglass.utlity.ActivityUtils
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.Locale
import kotlin.system.exitProcess


class HomeView(private val context: Context, private val appCompatActivity: ComponentActivity) {
    private var tts: TextToSpeech? = null
    private var serviceIntent: Intent? = null
    private var geminiClient = GeminiApiClient()

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun BottomSheetScaffoldState(
        navController: NavController,
        activity: ComponentActivity,
        googleAuthUiClient: GoogleAuthUiClient,
        vm: FirebaseViewModel
    ) {
        val viewModel = viewModel<ViewModelPhoto>()
        val bitmaps by viewModel.bitmaps.collectAsState()
        val vGeminiModel = GeminiViewModel()
        var geminiViewModel = ViewModelProvider(appCompatActivity)[vGeminiModel.javaClass]
       // val vKeyDownEventModel = KeyDownEventViewModel
        var keyDownEventViewModel =
            ViewModelProvider(appCompatActivity)[KeyDownEventViewModel::class.java]
        val scaffoldState = rememberBottomSheetScaffoldState()
        val controller: LifecycleCameraController = remember {
            LifecycleCameraController(context).apply {
                setEnabledUseCases(
                    CameraController.IMAGE_CAPTURE
                )
            }
        }

        tts = TextToSpeech(context, TextToSpeech.OnInitListener {
            tts?.language = Locale.getDefault()
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    // Speech started
                    Log.e("@onStrat", "speak started")
                }

                override fun onDone(utteranceId: String?) {
                    // Speech completed

                    Log.e("@onDone", "speak completed")
                    startVoiceRecognition()


                }

                override fun onError(utteranceId: String?) {
                    // Speech error
                    startVoiceRecognition()
                }
            })


        })


        LaunchedEffect(Unit) {
            startVoiceRecognition()

           // val vModel = VoiceCommandViewModel
            val voiceCommandViewModel = ViewModelProvider(appCompatActivity)[VoiceCommandViewModel::class.java]

            // Observe the LiveData object for changes
            voiceCommandViewModel.dataLiveData.observe(appCompatActivity) { newData ->
                // Update the UI with the new data
                if (newData.contains("detect")) {
                    takePhoto(controller, viewModel::onTakePhoto, "object", "")
                    //  startVoiceRecognition()
                } else if (newData.contains("money")) {
                    takePhoto(controller, viewModel::onTakePhoto, "currency", "")
                    //  startVoiceRecognition()
                } else if (newData.contains("describe")) {
                    takePhoto(controller, viewModel::onTakePhoto, "describe", "")
                } else if (newData.contains("read")) {
                    takePhoto(controller, viewModel::onTakePhoto, "text", "")
                } else if (newData.contains("find")) {
                    ObjectDetectFinderList.objectList.forEach { data ->
                        if (newData.contains(data)) {
                            takePhoto(controller, viewModel::onTakePhoto, "find", data)
                        }
                    }
                } else if (newData.contains("summary")) {
                    takePhoto(controller, viewModel::onTakePhoto, "summarize", "")
                } else if (newData.contains("face")) {
                    takePhoto(controller, viewModel::onTakePhoto, "face", "")
                } else {
                    geminiViewModel.dataLiveData.observe(appCompatActivity) { data ->
                        gemini(data)
                    }

                }


            }
            keyDownEventViewModel.dataLiveData.observe(appCompatActivity) { data ->
                if (data == "true") {
                    tts?.stop()
                    startVoiceRecognition()
                }

                //geminiViewModel.dataLiveData.removeObservers(activity)

            }
            //val Model = TakePhotoFirebaseViewModel
            val takePhotoFirebaseViewModel = ViewModelProvider(appCompatActivity)[TakePhotoFirebaseViewModel::class.java]

            // Observe the LiveData object for changes
            takePhotoFirebaseViewModel.dataLiveData.observe(appCompatActivity) { newData ->
                if (newData.contains("#")) {
                    takePhoto(controller, viewModel::onTakePhoto, "#", "")
                }

            }


        }

        YourDestinationScreen(navController, activity)
        SettingSection(scaffoldState, bitmaps, controller, navController)


    }


    private fun startVoiceRecognition() {
        serviceIntent = Intent(context, VoiceRecognitionService::class.java)

        context.startService(serviceIntent)
    }

    private fun gemini(newData: String) {

        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            geminiClient.aiAssistant(newData) { reply ->
                Log.e("Gemini", reply?.removePrefix("*").toString())
                speak(reply?.removePrefix("*"))
            }

        }
    }


    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun SettingSection(
        scaffoldState: BottomSheetScaffoldState,
        bitmaps: List<Bitmap>,
        controller: LifecycleCameraController,
        navController: NavController,

        ) {

        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetPeekHeight = 0.dp,
            sheetContent = {},


            ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.TopEnd
            ) {
                CameraPreview(
                    controller,
                    modifier = Modifier.fillMaxSize()
                )

                IconButton(
                    onClick = {
                        navController.navigate("/setting")

                    },
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.baseline_settings_24
                        ),
                        contentDescription = stringResource(R.string.SettingsIcon),
                        tint = Color.White
                    )
                }


            }


        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun YourDestinationScreen(navController: NavController, activity: ComponentActivity) {
        BackHandler {
            navController.popBackStack()
            activity.stopService(Intent(activity, VoiceRecognitionService::class.java))
            exitApp()
        }
        DisposableEffect(key1 = Unit) {
            onDispose {
            }
        }
    }

    @Composable
    private fun CameraPreview(
        controller: LifecycleCameraController,
        modifier: Modifier = Modifier
    ) {
        val lifecycleOwner = LocalLifecycleOwner.current
        AndroidView(
            factory = {
                PreviewView(it).apply {
                    this.controller = controller
                    controller.bindToLifecycle(lifecycleOwner)
                }
            },
            modifier = modifier
        )

    }


    private fun takePhoto(
        controller: LifecycleCameraController,
        onPhotoTaken: (Bitmap) -> Unit, mode: String, objectFinder: String
    ) {
        var photo: File? = null
        var fireStorageFile = ViewModelProvider(ActivityUtils.appCompatActivity!!)[FireStorageViewModel::class.java]
        val photoFile = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "${System.currentTimeMillis()}.jpg"
        )
        controller.takePicture(
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageCapturedCallback() {
                @SuppressLint("CheckResult")
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)

                    val matrix = Matrix().apply {
                        postRotate(image.imageInfo.rotationDegrees.toFloat())
                    }
                    val rotatedBitmap = Bitmap.createBitmap(
                        image.toBitmap(),
                        0,
                        0,
                        image.width,
                        image.height,
                        matrix,
                        true,
                    )

                    photo = savePhotoToGallery(rotatedBitmap)
                    image.close()
                    if (mode != "#") {
                        uploadPhoto(photo!!, mode, objectFinder).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ data ->
                                // tts?.speak(data[0].result, TextToSpeech.QUEUE_FLUSH, null, null)
                                speak(data[0].result)
                                Toast.makeText(context, data[0].result, Toast.LENGTH_LONG).show()
                            }, { error ->

                            })
                    } else if (mode == "#") {
                        Log.e("mode fire", photo!!.toUri().toString())
                        val db = FirebaseFirestore.getInstance()
                        val storageRef = FirebaseStorage.getInstance().reference
                        val photoRef =
                            FirebaseStorage.getInstance().getReference("photos/${photo?.name}.jpg")

                        photoRef.putFile(photo!!.toUri())
                            .addOnSuccessListener {
                                Log.e("addOnSuccessListener", "Photo uploaded successfully")
                                // Once the photo is uploaded, save its download URL to Firestore or perform any other necessary actions
                                photoRef.downloadUrl.addOnSuccessListener { uri ->
                                    // Save the download URL to Firestore or perform other actions
                                    val photoUrl = uri.toString()
                                    Log.e("photoUrl", "  $photoUrl")

                                    fireStorageFile.updateData(photoUrl)

                                }.addOnFailureListener { exception ->
                                    Log.e(
                                        "addOnFailureListener",
                                        "Error getting download URL",
                                        exception
                                    )
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.e("OnFailureListener", "Error uploading photo", exception)
                            }
                    }

                    //Toast.makeText(context,"${photo?.absoluteFile}",Toast.LENGTH_SHORT).show()

                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e("Camera", "Couldn't take photo: ", exception)
                }
            }
        )

    }


    @SuppressLint("SuspiciousIndentation")
    private fun savePhotoToGallery(bitmap: Bitmap): File? {
        val filename = "${System.currentTimeMillis()}.jpg"
        val outputStream: OutputStream?
        var image: File? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Use MediaStore API to save the photo to the gallery
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            val imageUri =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            if (imageUri != null) {
                image = File(getImagePathFromUri(imageUri))

            }

            outputStream = imageUri?.let { resolver.openOutputStream(it) }
        } else {
            // Save the photo to the Pictures directory
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            image = File(imagesDir, filename)
            outputStream = FileOutputStream(image)
        }

        outputStream?.use { stream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
        }
        return image
    }

    fun uploadPhoto(
        photo: File,
        mode: String,
        objectFinder: String
    ): Observable<ArrayList<UploadResponse>> {

        val requestFile: RequestBody =
            photo.asRequestBody("multipart/from-data".toMediaType())
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", photo.name, requestFile)
        val selectMode = mode.toRequestBody("text/plain".toMediaType())
        val objectBeFind = objectFinder.toRequestBody("text/plain".toMediaType())
        val call: Observable<ArrayList<UploadResponse>> =
            SmartGlassApi().uploadImage(body, selectMode, objectBeFind)
        return call


    }

    private fun getImagePathFromUri(uri: Uri): String? {
        var path: String? = null
        val context: Context = context
        val contentResolver: ContentResolver = context.contentResolver
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            if (cursor.moveToFirst()) {
                path = cursor.getString(columnIndex)
            }

        }
        cursor?.close()
        return path
    }


    private fun exitApp() {
        exitProcess(0)
    }

    private fun speak(text: String?) {

        val params = Bundle()
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "messageID")


        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, "messageID")


    }


}