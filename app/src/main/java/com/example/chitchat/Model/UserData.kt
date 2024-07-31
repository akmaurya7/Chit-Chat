package com.example.chitchat.Model

import android.net.Uri


data class  User(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val imageUri: String = ""
)