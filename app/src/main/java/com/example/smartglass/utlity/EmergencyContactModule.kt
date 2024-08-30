package com.example.smartglass.utlity

import android.app.Application
import com.example.smartglass.data.viewmodel.EmergencyContactViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object EmergencyContactModule {
    @Provides
    fun emergencyContactViewModel(application: Application): EmergencyContactViewModel {
        return EmergencyContactViewModel(application)
    }
}