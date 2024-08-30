package com.example.smartglass.data.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.smartglass.data.model.EmergencyContactModel

class EmergencyContactViewModel(application: Application):RepositoryViewModel(application) {

    fun insertEmergencyContact(emergencyContactModel: EmergencyContactModel){
        repository.insertEmergencyContact(emergencyContactModel)
    }

    fun getEmergencyContact(): LiveData<List<EmergencyContactModel>> = repository.getEmergencyContact()

    fun deleteAllContact(){
        repository.deleteAllContact()
    }

}