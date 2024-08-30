package com.example.smartglass.data.model

data class GoogleSignInResult(
    val data: UserDataFromGoogleAccount?,
    val errorMessage: String?
)

data class UserDataFromGoogleAccount(
    val userId: String,
    val username: String?,
    val email: String
)


