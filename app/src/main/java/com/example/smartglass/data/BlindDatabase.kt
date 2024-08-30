package com.example.smartglass.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.smartglass.data.model.ConnectedDeviceContactModel
import com.example.smartglass.data.model.EmergencyContactModel
import com.example.smartglass.data.model.UserProfileModel
import com.example.smartglass.data.interfaceblind.BlindDao
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [UserProfileModel::class, EmergencyContactModel::class,ConnectedDeviceContactModel::class], version = 8)
abstract class BlindDatabase : RoomDatabase() {
    abstract fun blindDao(): BlindDao

    companion object {
        @Volatile
        var instance: BlindDatabase? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getDataBaseInstance(context: Context): BlindDatabase {
            val tempInstance = instance
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val roomDataBaseInstance = Room.databaseBuilder(
                    context,
                    BlindDatabase::class.java,
                    "Blind"
                ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
                instance = roomDataBaseInstance
                return roomDataBaseInstance
            }
        }
    }

}