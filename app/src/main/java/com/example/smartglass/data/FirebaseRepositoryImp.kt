package com.example.smartglass.data

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.smartglass.data.model.ConnectedDeviceContactModel
import com.example.smartglass.data.model.LocationDegree
import com.example.smartglass.data.viewmodel.ConnectedDeviceContactViewModel
import com.example.smartglass.data.viewmodel.FireStorageViewModel
import com.example.smartglass.data.viewmodel.TakePhotoFirebaseViewModel
import com.example.smartglass.data.interfaceblind.FirebaseRepository
import com.example.smartglass.utlity.ActivityUtils
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date
import kotlin.math.abs

class FirebaseRepositoryImp(private  val application: Application) : FirebaseRepository {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val dataReferenceConnectedUsers: DatabaseReference =
        database.getReference("connectedUsers")
    private val dataReferenceMessages: DatabaseReference = database.getReference("messages")


    override fun connectedChange(personalId:String,onComplete: () -> Unit) {
        val blindId = FirebaseAuth.getInstance().currentUser?.uid?.take(4)
        dataReferenceConnectedUsers.child("connectedUser").endAt(blindId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.children.forEach { children ->
                        val key = children.key

                        val connectedChange = children.child("connected").getValue(Int::class.java)
                        val personId = children.child("personId").getValue(String::class.java)
                        //personId.st
                        if (key != null) {
                            if (connectedChange == 0 && key.takeLast(4) == blindId && key.contains(personalId)) {
                                Log.e("key",key.toString())
                                    children.child("connected").ref.setValue(1)
                                Log.e("index $personalId",personalId)
                                    onComplete()

                            }
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle onCancelled if needed
                }
            })
    }

    override fun getConnectedDeviceContact() {
        val blindId = FirebaseAuth.getInstance().currentUser?.uid?.take(4)

        dataReferenceConnectedUsers.child("connectedUser").endAt(blindId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (childSnapshot in dataSnapshot.children) {
                        val connectedValue =
                            childSnapshot.child("connected").getValue(Int::class.java)
                        if (connectedValue == 1) {
                            val blindEmail =
                                childSnapshot.child("blindEmail").getValue(String::class.java)
                            val blindID =
                                childSnapshot.child("blindId").getValue(String::class.java)
                            val personEmail =
                                childSnapshot.child("personEmail").getValue(String::class.java)
                            val personId =
                                childSnapshot.child("personId").getValue(String::class.java)
                            val connected =
                                childSnapshot.child("connected").getValue(Int::class.java)
                            val connectedDeviceContactModel = ConnectedDeviceContactModel(
                                blindId = blindID!!,
                                blindEmail = blindEmail!!,
                                personId = personId!!,
                                personEmail = personEmail!!,
                                connected = connected!!,
                            )
                            val existContact = ConnectedDeviceContactViewModel(application)
                                .getConnectedDeviceContactByPersonId(personId)
                            Log.i("why is null", "$existContact")
                            if (existContact == null) {
                                ConnectedDeviceContactViewModel(application)
                                    .insertConnectedDeviceContact(connectedDeviceContactModel)
                            }

                        }

                    }
                }

                override fun onCancelled(p0: DatabaseError) {

                }

            })
    }

    fun messageSender() {
        val viewModel = ViewModelProvider(ActivityUtils.appCompatActivity!!)[TakePhotoFirebaseViewModel::class.java]
        //val vModel = FireStorageViewModel
        val fireStorageViewModel = ViewModelProvider(ActivityUtils.appCompatActivity!!)[FireStorageViewModel::class.java]
        val user = FirebaseAuth.getInstance().currentUser
        val userFirstPartId = user?.uid
        val blindEmail = user?.email
        getPersonId { personId ->
            val getConnectedContact =
                personId?.let {
                    ConnectedDeviceContactViewModel(application).getConnectedDeviceContactByPersonId(
                        it
                    )
                }
            val userSecondPartId = getConnectedContact?.personId

            var idCheck = "${userSecondPartId}${userFirstPartId?.take(4)}"

            dataReferenceMessages.child("message").endAt(userFirstPartId?.take(4)).addValueEventListener(
                object : ValueEventListener{
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                      //  Toast.makeText(application, "$idCheck  $userSecondPartId",Toast.LENGTH_LONG).show()
                        dataSnapshot.children.forEach { children ->
                            val userId = children.key
                            val valueContain = children.child("isRead").getValue(Int::class.java)
                            val enableID = children.child("enable").getValue(Int::class.java)
                            if (userId!=null && userId==idCheck ){

                                if (valueContain == 1 && enableID==1){
                                    Log.e("@ah","onDataChange")
                                    viewModel.updateData("#")
                                    Log.e("ahmed","a7a2")


                                    // Observe the LiveData object for changes
                                    fireStorageViewModel.dataLiveData.observe(ActivityUtils.appCompatActivity!!) { newData ->
                                        val enableID = children.child("enable").getValue(Int::class.java)
                                        if ( enableID==1) {

                                            Log.e("ahmed","a7a3")
                                            createMessage(
                                                userSecondPartId,
                                                userFirstPartId,
                                                "1",
                                                1, 0
                                            )
                                            createMessage(
                                                userFirstPartId,
                                                userSecondPartId,
                                                newData,
                                                1,
                                                1
                                            )
                                        }
                                    }

                                }else if(valueContain==2 && enableID==1){
                                  //  createMessage(userFirstPartId,userSecondPartId,"")
                                    fetchLocation( userFirstPartId, userSecondPartId,)

                                }else if (enableID==0){
                                    viewModel.updateData("")
                                }

                            }


                        }
                     //   dataReferenceMessages.child("message").removeEventListener(this)

                    }

                    override fun onCancelled(p0: DatabaseError) {
                        Log.e("onCancelled",p0.message)
                    }

                }
            )




        }

    }
    @SuppressLint("MissingPermission")
    private fun fetchLocation(userFirstPartId:String?,userSecondPartId:String?) {


        var fusedLocationClient = LocationServices.getFusedLocationProviderClient(ActivityUtils.acitvityComponant)
        fusedLocationClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
             null
        )
            .addOnSuccessListener { location ->
                // Got last known location. In some rare situations, this can be null.
                if (location != null) {
                    var locationDirection=getDirectionFromPoint(location)
                    var locationUrl="https://www.google.com/maps/place/${convertToDMS(location.latitude)}${locationDirection.latitudeDirection}" +
                            "+${convertToDMS(location.longitude)}${locationDirection.longitudeDirection}"
                    createMessage( userFirstPartId, userSecondPartId, locationUrl,2,1)
                    createMessage(userSecondPartId , userFirstPartId, "1", 2,0)
                }
            }
    }
    private fun convertToDMS(decimalDegree: Double): String {
        val degrees = decimalDegree.toInt()
        val minutesDecimal = (abs(decimalDegree) - abs(degrees)) * 60
        val minutes = minutesDecimal.toInt()
        val seconds = (minutesDecimal - minutes) * 60

        return "$degrees° $minutes' $seconds\""
    }
    private fun getDirectionFromPoint(location:Location): LocationDegree {

        val referenceLocation = Location("").apply {
            // Set the reference location to your desired reference point
            latitude = 27.17822
            longitude = 31.18601
        }

        val directionLat = if (location.latitude > referenceLocation.latitude) {
         "N"
        } else {
               "S"
        }

        val directionLong = if (location.longitude > referenceLocation.longitude) {
             "E"
        } else {
               "W"
        }
return LocationDegree(directionLat,directionLong)
    }

    fun createMessage(userFirstPartId:String? , userSecondPartId:String? , value:String,commandID:Int,enable:Int){
        val time = System.currentTimeMillis()
        val timeStamp = Date(time)

        var messageInfo = mapOf(
            "senderId" to userFirstPartId,
            "receiverId" to userSecondPartId,
            "timeStamp" to timeStamp,
            "value" to value ,
            "isRead" to commandID ,
            "enable" to enable
        )
        var id = "${userFirstPartId}${userSecondPartId?.take(4)}"
        dataReferenceMessages.child("message").child(id).setValue(messageInfo)

    }

    private fun getPersonId( callback: (String?) -> Unit ) {
        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid?.take(4)
        dataReferenceMessages.child("message").endAt(userId).addListenerForSingleValueEvent(
            object : ValueEventListener {
                @SuppressLint("SuspiciousIndentation")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var personId: String
                    for (childSnapshot in dataSnapshot.children) {
                        val nodeId = childSnapshot.key
                        val isRead = childSnapshot.child("isRead").getValue(Int::class.java)
                        if (nodeId?.takeLast(4) == userId && isRead==1) {
                            personId = childSnapshot.child("senderId").value as String
                                callback(personId)
                        }else if (nodeId?.takeLast(4) == userId && isRead==2){
                            personId = childSnapshot.child("senderId").value as String
                            callback(personId)
                        }

                    }
                    dataReferenceMessages.child("message").removeEventListener(this)


                }

                override fun onCancelled(p0: DatabaseError) {
                    // Handle cancellation if needed
                    callback(null)
                }
            })
    }
    override fun isReadChange(){
        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid?.take(4)
        dataReferenceMessages.child("message").endAt(userId).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.children.forEach { children ->
                        val key = children.key
                        val isRead = children.child("isRead").getValue(Int::class.java)
                        if (isRead == 0 && key?.takeLast(4) == userId) {
                            if (key != null) {
                                children.child("isRead").ref.setValue(1)
                                Log.e("isRead","${children.child("isRead").value }")
                                return@forEach


                            }


                        }

                    }
                }

                override fun onCancelled(dataSnapshot: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }


}