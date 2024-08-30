package com.example.smartglass.data.interfaceblind

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.smartglass.data.model.ConnectedDeviceContactModel
import com.example.smartglass.data.model.EmergencyContactModel
import com.example.smartglass.data.model.UserProfileModel

@Dao
interface BlindDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserProfile(userProfile:UserProfileModel)

    @Query("Select * from UserProfile")
    fun getUserProfile():LiveData<List<UserProfileModel>>

    @Query("Select * from UserProfile")
    fun getUserProfileSingleData():LiveData<UserProfileModel>

    @Query("Delete From UserProfile")
        fun deleteAll()




    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEmergencyContact(emergencyContactModel: EmergencyContactModel)

    @Query("select * from EmergencyContact")
    fun getEmergencyContact():LiveData<List<EmergencyContactModel>>

    @Query("delete from EmergencyContact")
    fun deleteAllContact()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertConnectedDeviceContact(connectedDeviceContactModel: ConnectedDeviceContactModel)

    @Query("select * from ConnectedDeviceContact")
    fun getConnectedDeviceContact(): LiveData<List<ConnectedDeviceContactModel>>

    @Query("delete  from ConnectedDeviceContact")
    fun deleteAllConnectedDeviceContact()

    @Query("select * from ConnectedDeviceContact where personId = :personId LiMiT 1")
    fun getConnectedDeviceContactByPersonId(personId:String):ConnectedDeviceContactModel?

    @Query("select * from ConnectedDeviceContact where blindId = :blindId LiMiT 1")
    fun getConnectedDeviceContactByIdBlind(blindId:String):ConnectedDeviceContactModel?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertConnectedDeviceContactIfNotExists(connectedDeviceContactModel: ConnectedDeviceContactModel)

    



}