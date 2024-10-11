package com.example.smartglass.data.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.smartglass.data.Notification
import com.example.smartglass.utlity.PersonalIdNotify
import com.example.smartglass.utlity.NotificationHashCode
import com.example.smartglass.utlity.TypeIdUserLogin
import com.example.smartglass.utlity.UriCache
import com.example.smartglass.data.model.ConnectedDeviceContactModel
import com.example.smartglass.data.model.EmergencyContactModel
import com.example.smartglass.data.model.UserProfileModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class FirebaseViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val userProfileViewModel: UserProfileViewModel,
    private val emergencyContactViewModel: EmergencyContactViewModel,
    private val connectedDeviceContactViewModel: ConnectedDeviceContactViewModel

) : ViewModel() {


    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val dataReferenceUser: DatabaseReference = database.getReference("users")
    private val dataReferenceConnectedUsers: DatabaseReference =
        database.getReference("connectedUsers")
    private val dataReferenceMessages: DatabaseReference = database.getReference("messages")
    val signedIn = mutableStateOf(false)
    private val inProcess = mutableStateOf(false)
    val popupNotification = mutableStateOf(null)
    val isRestPassword = mutableStateOf(false)


    fun onSignup(
        user: UserProfileModel,
        email: String,
        password: String,
        callback: (Boolean) -> Unit
    ) {
        val encodeEmail = user.email.replace(".", "_").replace("@", "_")
        inProcess.value = true
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val firebaseUser = it.result?.user
                val uid = firebaseUser?.uid
                val userprofileMap = mapOf(
                    "fullName" to user.fullName,
                    "userName" to user.userName,
                    "email" to user.email,
                    "emailID" to uid,
                    "phoneNumber" to user.phoneNumber,
                    "password" to user.password,
                    "typeId" to user.typeId,
                    "uri" to user.uri
                )
                callback(true)
                dataReferenceUser.child(encodeEmail).setValue(userprofileMap)
                signedIn.value = true

            } else {
                callback(false)
                Log.e("signUp", "the email already used")
            }
            inProcess.value = false
        }
    }

    fun login(email: String, password: String, context: Context, callback: (Boolean) -> Unit) {
        inProcess.value = true

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    signedIn.value = true
                    callback(true)
                    Toast.makeText(context, "The Login successful", Toast.LENGTH_SHORT).show()
                    Log.e("Login", "success to log in: $auth")
                } else {
                    Log.e("LoginFailed", "Failed to log in: $auth")
                    callback(false)
                    Toast.makeText(
                        context,
                        "The Login Failed Check Email or Password ",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                inProcess.value = false
            }
    }

    fun uploadImage(email: String, uri: String) {
        val encodeEmail = email.replace(".", "_").replace("@", "_")
        dataReferenceUser.child(encodeEmail).child("uri").setValue(uri)
    }

    fun uploadEmailGoogleToDatabase(emailGoogle: String, name: String, idGoogle: String) {
        val encodeEmail = emailGoogle.replace(".", "_").replace("@", "_")

        val userRef = dataReferenceUser.child(encodeEmail)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    val user = mapOf(
                        "email" to emailGoogle,
                        "fullName" to name,
                        "emailID" to idGoogle,
                        "typeId" to 0,
                        "uri" to "",
                    )
                    userRef.setValue(user)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.e("onCancelld",p0.message)
            }
        })
    }


    fun uploadContacts(email: String, emergencyContacts: List<EmergencyContactModel>) {
        val encodeEmail = email.replace(".", "_").replace("@", "_")
        val contactRef = dataReferenceUser.child(encodeEmail).child("userContacts")
        emergencyContacts.forEach { contact ->
            val contactData = mapOf(
                "fullName" to contact.fullName,
                "phoneNumber" to contact.phoneNumber
            )
            val contactKey = contactRef.push().key
            if (contactKey != null) {
                contactRef.child(contactKey).setValue(contactData)
            }
        }


    }

    fun uploadImageGoogleEmail(googleEmail: String, googleEmailId: String, uri: String) {
        val encodeEmail = googleEmail.replace(".", "_").replace("@", "_")
        val currentUserId = FirebaseAuth.getInstance().currentUser
        val idGoogle = currentUserId?.uid
        if (googleEmailId == idGoogle) {
            dataReferenceUser.child(encodeEmail).child("uri").setValue(uri)
        }
    }

    fun getImageFromGoogleEmail(googleEmail: String, googleEmailId: String) {
        val encodeEmail = googleEmail.replace(".", "_").replace("@", "_")
        val currentUserId = FirebaseAuth.getInstance().currentUser
        val idGoogle = currentUserId?.uid
        if (googleEmailId == idGoogle) {
            dataReferenceUser.child(encodeEmail).child("uri")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val userData = dataSnapshot.getValue(String::class.java)
                        if (userData != null) {
                            UriCache.uri = userData
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {
                        Log.e("onCancelld",p0.message)
                    }
                })

        }


    }

    fun getTypeIdUserLogin(email: String) {

        val encodeEmail = email.replace(".", "_").replace("@", "_")
        if (encodeEmail.isNotEmpty()) {

            dataReferenceUser.child(encodeEmail).child("typeId")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val userData = dataSnapshot.getValue(Int::class.java)
                        if (userData != null) {
                            TypeIdUserLogin.typeIdUserLogin = userData
                        }
                    }

                    override fun onCancelled(dataSnapshot: DatabaseError) {
                        Log.e("onCancelld",dataSnapshot.message)
                    }

                })
        }


    }

    suspend fun getIdTypeUserGoogleAccount(
        googleEmail: String,
        googleEmailId: String,
    ): Int? {
        val encodeEmail = googleEmail.replace(".", "_").replace("@", "_")
        val currentUserId = FirebaseAuth.getInstance().currentUser
        val idGoogle = currentUserId?.uid
        if (googleEmailId == idGoogle) {
            val dataSnapshot = dataReferenceUser.child(encodeEmail).child("typeId").get().await()
            return dataSnapshot.getValue(Int::class.java)
        }
        return null

    }


    fun notificationShow(context: Context) {
        val blindId = FirebaseAuth.getInstance().currentUser?.uid?.take(4)
        val notification = Notification(context)

        dataReferenceConnectedUsers.child("connectedUser")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (childSnapshot in dataSnapshot.children) {
                        val userId = childSnapshot.key
                        val connectedValue = childSnapshot.child("connected")
                            .getValue(Int::class.java)
                        if (userId != null && userId.takeLast(4) == blindId && connectedValue == 0) {
                            var title = "The connection Device Notification"
                            var personId = childSnapshot.child("personId").getValue(String::class.java)

                            var personEmail = childSnapshot.child("personEmail").value as String
                            var message = "$personEmail send you request to connect"
                            //val notificationId = personEmail.hashCode()
                            notification.sendNotification(
                                title,
                                message,
                                "ACCEPT_ACTION",
                                "ACCEPT",
                                personId
                            )
                            if (personId != null) {
                                PersonalIdNotify.personalsId.add(personId)
                            }


                        }


                    }
                    dataReferenceConnectedUsers.child("connectedUser").removeEventListener(this)

                }

                override fun onCancelled(p0: DatabaseError) {
                    Log.e("onCancelld",p0.message)
                }

            })


    }

    fun notificationMessage(context: Context, application: Application) {
        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid?.take(4)
        val notification = Notification(context)
        dataReferenceMessages.child("message").endAt(userId).addValueEventListener(
            object : ValueEventListener {
                @SuppressLint("SuspiciousIndentation")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (childSnapshot in dataSnapshot.children) {
                        val nodeId = childSnapshot.key
                        val personId = childSnapshot.child("senderId").value as String
                        val isRead = childSnapshot.child("isRead").getValue(Int::class.java)
                        val getContactDevice = ConnectedDeviceContactViewModel(application)
                            .getConnectedDeviceContactByPersonId(personId)
                        if (nodeId != null && nodeId.takeLast(4) == userId && isRead == 0) {
                            val title = "The Parental Control Notification"
                            val personEmail = getContactDevice?.personEmail

                            val message = "$personEmail send you get access control"
                            val notificationId = personEmail.hashCode()
                            NotificationHashCode.value = notificationId
                                notification.sendNotification(
                                title,
                                message,
                                "Ok_ACTION",
                                "Ok",
                                    personId
                            )

                        }
                    }


                }

                override fun onCancelled(dataSnapshot: DatabaseError) {
                    Log.e("onCancelld",dataSnapshot.message)
                }

            }
        )

    }


    fun acceptedNotificationConnected() {
        val blindId = FirebaseAuth.getInstance().currentUser?.uid?.take(4)
        dataReferenceConnectedUsers.child("connectedUser")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.children.forEach { childSnapshot ->
                        Log.e("userId", blindId.toString())
                        val userId = childSnapshot.key
                        val connectedValue = childSnapshot.child("connected")
                            .getValue(Int::class.java)
                        if (userId != null && userId.takeLast(4) == blindId && connectedValue == 1) {
                            val connectedDeviceContact = childSnapshot
                                .getValue(ConnectedDeviceContactModel::class.java)
                            connectedDeviceContact?.let {
                                connectedDeviceContactViewModel.insertConnectedDeviceContact(it)
                            }
                        }
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    Log.e("onCanceld",p0
                        .message)
                }

            })
    }


    fun fetchContactsWithEmailGoogleFromFirebase(gmailGoogle: String) {
        val encodeEmail = gmailGoogle.replace(".", "_").replace("@", "_")
        dataReferenceUser.child(encodeEmail).child("userContacts")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    for (contacts in p0.children) {
                        val emergencyContact = contacts
                            .getValue(EmergencyContactModel::class.java)
                        emergencyContact?.let {
                            emergencyContactViewModel.insertEmergencyContact(it)
                        }
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    Log.e("onCancelld",p0.message)
                }

            })
    }

    fun fetchUserProfileFromFirebase(email: String) {
        val encodeEmail = email.replace(".", "_").replace("@", "_")
        dataReferenceUser.child(encodeEmail)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userProfileData = snapshot.getValue(UserProfileModel::class.java)

                    if (userProfileData != null) {
                        // Save user profile data to local Room database
                        userProfileViewModel.insertUserProfile(userProfileData)

                    } else {
                        Log.e("Firebase", "User profile data not found in Firebase database")
                    }
                    dataReferenceUser.child(encodeEmail).child("userContacts")
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(p0: DataSnapshot) {
                                //val emergencyContacts = mutableListOf<EmergencyContactModel>()
                                for (contacts in p0.children) {
                                    val emergencyContact = contacts
                                        .getValue(EmergencyContactModel::class.java)
                                    emergencyContact?.let {
                                        //emergencyContacts.add(it)
                                        emergencyContactViewModel.insertEmergencyContact(it)
                                    }

                                }


                            }

                            override fun onCancelled(p0: DatabaseError) {
                                Log.e("onCancelld",p0.message)
                            }

                        })
                }


                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Failed to fetch user profile data: ${error.message}")
                }
            })
    }

    fun isEmailRegistered(email: String, context: Context, onSuccess: (Boolean) -> Unit) {
        val encodeEmail = email.replace(".", "_").replace("@", "_")
        dataReferenceUser.child(encodeEmail)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    onSuccess(snapshot.exists())
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Your Email not registered  : ${error.message}")
                    Toast.makeText(context, "Your Email not registered", Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun sendPasswordResetEmail(email: String, onSuccess: () -> Unit) {

        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                isRestPassword.value = false
            }

    }

    fun updatedPasswordInDatabase(email: String, newPassword: String) {
        val encodeEmail = email.replace(".", "_").replace("@", "_")
        val userReference = database.getReference("users").child(encodeEmail)

        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    userReference.ref.child("password").setValue(newPassword)
                }
            }

            override fun onCancelled(snapshot: DatabaseError) {
                Log.e("onCancelld",snapshot.message)
            }
        })


    }


    fun isSignedIn(): Boolean {
        return auth.currentUser != null
    }

    fun signOut() {
        auth.signOut()
    }


}
