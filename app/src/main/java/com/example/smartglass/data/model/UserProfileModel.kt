package com.example.smartglass.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("UserProfile")
data class UserProfileModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val typeId :Int = 0,
    val emailID : String ="",
    val fullName: String = "",
    val userName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val password: String = "",
    var uri: String = "",

    ) {
    constructor() : this(0, 0,"","", "", "", "", "", "")

}
