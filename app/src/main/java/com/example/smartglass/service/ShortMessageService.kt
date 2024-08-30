package com.example.smartglass.service

import android.os.Build
import android.telephony.SmsManager
import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.smartglass.data.model.EmergencyContactModel
import com.example.smartglass.data.viewmodel.EmergencyContactViewModel

class ShortMessageService(private val appCompatActivity: ComponentActivity) {

    private val emergencyContactViewModel: EmergencyContactViewModel by lazy {
        ViewModelProvider(appCompatActivity)[EmergencyContactViewModel::class.java]
    }

    private var emergencyContactMutableList by mutableStateOf<List<EmergencyContactModel>>(emptyList())
    private val emergencyContactLiveData = emergencyContactViewModel.getEmergencyContact()
    private val observerEmergencyContactModel = Observer<List<EmergencyContactModel>> { newData ->
        emergencyContactMutableList = newData
    }


    fun smsEmergencyContact() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val phoneNumber = listOf(observerEmergencyContactModel)
            val message = "blind is disconnected"
            val smsManger = SmsManager.getDefault()
            emergencyContactLiveData.observe(appCompatActivity) { contacts ->
                contacts.forEach { contact ->
                    smsManger.sendTextMessage(contact.phoneNumber, null, message, null, null)
                }
            }


        }

    }
}