package com.example.smartglass.service

import android.app.Application
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.smartglass.data.FirebaseRepositoryImp
import com.example.smartglass.utlity.NotificationHashCode

class NotificationBroadcastReceiver : BroadcastReceiver() {
    companion object {
        var index:Int = 0
    }

    override fun onReceive(context: Context, intent: Intent) {

        when (intent.action) {
            "ACCEPT_ACTION" -> {
                val application = context.applicationContext as Application
                val repository = FirebaseRepositoryImp(application)
                var personId = intent.getStringExtra("personId")

                if (personId != null) {
                    repository.connectedChange(personId){
                        repository.getConnectedDeviceContact()
                    }
                }

              //  Log.e("index $index",PersonalIdNotify.personalsId[index])
            //    Log.e("index $index",PersonalIdNotify.personalsId.toString())
              //  index++

                Toast.makeText(context, "Notification accepted", Toast.LENGTH_SHORT).show()
                val notificationId = intent.getIntExtra("notificationId" ,-1)

                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                if (notificationId!=-1){
                    Log.e("notificationId",notificationId.toString())
                    notificationManager.cancel(notificationId)
                }




            }
            "Ok_ACTION" -> {
                val application =context.applicationContext as Application
                val repository = FirebaseRepositoryImp(application)
                repository.isReadChange()
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.cancel(NotificationHashCode.value)
            }
        }


    }
}