package com.example.smartglass.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.smartglass.data.BlindDatabase
import com.example.smartglass.data.BlindRepository


open class RepositoryViewModel(application: Application) : AndroidViewModel(application) {
    protected val repository: BlindRepository

    init {
        val blindDao = BlindDatabase.getDataBaseInstance(application).blindDao()
        repository = BlindRepository(blindDao)
    }

}