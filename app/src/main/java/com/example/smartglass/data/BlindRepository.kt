package com.example.smartglass.data

import androidx.lifecycle.LiveData
import com.example.smartglass.data.model.ConnectedDeviceContactModel
import com.example.smartglass.data.model.EmergencyContactModel
import com.example.smartglass.data.model.UserProfileModel
import com.example.smartglass.data.interfaceblind.BlindDao

class BlindRepository(private val blindDao: BlindDao) {

    fun insertUserProfile(userProfileModel: UserProfileModel) {
        blindDao.insertUserProfile(userProfileModel)
    }

    fun getUserProfile(): LiveData<List<UserProfileModel>> {
        return blindDao.getUserProfile()
    }

    fun getUserProfileSingleData(): LiveData<UserProfileModel> {
        return blindDao.getUserProfileSingleData()
    }

    fun deleteAll() {
        blindDao.deleteAll()
    }


    fun insertEmergencyContact(emergencyContactModel: EmergencyContactModel) {
        blindDao.insertEmergencyContact(emergencyContactModel)
    }

    fun getEmergencyContact(): LiveData<List<EmergencyContactModel>> {
        return blindDao.getEmergencyContact()
    }

    fun deleteAllContact() {
        blindDao.deleteAllContact()
    }

    fun insertConnectedDeviceContact(connectedDeviceContactModel: ConnectedDeviceContactModel) {
        blindDao.insertConnectedDeviceContact(connectedDeviceContactModel)
    }
    fun insertConnectedDeviceContactIfNotExists(connectedDeviceContactModel: ConnectedDeviceContactModel){
        blindDao.insertConnectedDeviceContactIfNotExists(connectedDeviceContactModel)
    }
    fun getConnectedDeviceContact(): LiveData<List<ConnectedDeviceContactModel>> {
        return blindDao.getConnectedDeviceContact()
    }

    fun deleteAllConnectedDeviceContact() {
        blindDao.deleteAllConnectedDeviceContact()
    }
    fun getConnectedDeviceContactByPersonId(personId:String):ConnectedDeviceContactModel?{
        return blindDao.getConnectedDeviceContactByPersonId(personId)
    }
    fun getConnectedDeviceContactByIdBlind(blindId:String):ConnectedDeviceContactModel?{
        return blindDao.getConnectedDeviceContactByIdBlind(blindId)
    }


}