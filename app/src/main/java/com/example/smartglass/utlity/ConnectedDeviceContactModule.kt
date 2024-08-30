package com.example.smartglass.utlity

import android.app.Application
import com.example.smartglass.data.viewmodel.ConnectedDeviceContactViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ConnectedDeviceContactModule {
    @Provides
    fun connectedDeviceContactViewModel(application: Application):ConnectedDeviceContactViewModel{
        return ConnectedDeviceContactViewModel(application)
    }
}