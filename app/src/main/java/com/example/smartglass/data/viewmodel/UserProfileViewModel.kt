package com.example.smartglass.data.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.smartglass.data.model.UserProfileModel

class UserProfileViewModel(application: Application) : RepositoryViewModel(application) {

    fun insertUserProfile(userProfileModel: UserProfileModel) {
        repository.insertUserProfile(userProfileModel)
    }

    fun getUserProfile(): LiveData<List<UserProfileModel>> = repository.getUserProfile()

    fun getUserProfileSingleData(): LiveData<UserProfileModel> =
        repository.getUserProfileSingleData()

    fun deleteAll() {
        repository.deleteAll()

    }


}