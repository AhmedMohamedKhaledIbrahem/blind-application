package com.example.smartglass.data.interfaceblind

import android.app.Application

interface FirebaseRepository {

    fun connectedChange(personalId:String,onComplete:()->Unit,)
    fun getConnectedDeviceContact()
    fun isReadChange()
}