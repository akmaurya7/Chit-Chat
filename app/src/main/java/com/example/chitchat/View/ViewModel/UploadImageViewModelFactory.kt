package com.example.chitchat.View.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UploadImageViewModelFactory(private val auth: FirebaseAuth,private val db:FirebaseFirestore) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UploadImageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UploadImageViewModel(auth,db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}