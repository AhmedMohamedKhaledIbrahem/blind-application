package com.example.smartglass.data.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.smartglass.data.model.ConnectedDeviceContactModel

class ConnectedDeviceContactViewModel(application: Application) : RepositoryViewModel(application) {

    fun insertConnectedDeviceContact(connectedDeviceContactModel: ConnectedDeviceContactModel) {
        repository.insertConnectedDeviceContact(connectedDeviceContactModel)
    }
    fun insertConnectedDeviceContactIfNotExists(connectedDeviceContactModel: ConnectedDeviceContactModel){
        repository.insertConnectedDeviceContactIfNotExists(connectedDeviceContactModel)
    }

    fun getConnectedDeviceContact(): LiveData<List<ConnectedDeviceContactModel>> {
        return repository.getConnectedDeviceContact()
    }

    fun deleteAllConnectedDeviceContact() {
        repository.deleteAllConnectedDeviceContact()
    }
    fun getConnectedDeviceContactByPersonId(personId:String):ConnectedDeviceContactModel?{
        return repository.getConnectedDeviceContactByPersonId(personId)
    }
    fun getConnectedDeviceContactByIdBlind(blindId:String):ConnectedDeviceContactModel?{
        return repository.getConnectedDeviceContactByIdBlind(blindId)
    }


}