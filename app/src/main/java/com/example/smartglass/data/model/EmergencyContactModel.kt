package com.example.smartglass.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity("EmergencyContact")
data class EmergencyContactModel(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val fullName:String,
    val phoneNumber:String,
){
    constructor():this(0,"","")
}