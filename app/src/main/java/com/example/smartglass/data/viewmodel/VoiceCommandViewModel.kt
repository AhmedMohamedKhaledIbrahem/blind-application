package com.example.smartglass.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

object VoiceCommandViewModel : ViewModel() {
    private val _dataLiveData = MutableLiveData<String>()


    val dataLiveData: LiveData<String>
        get() = _dataLiveData

    // Function to update the LiveData object with new data
    fun updateData(newData: String) {
        _dataLiveData.value = newData
    }
}