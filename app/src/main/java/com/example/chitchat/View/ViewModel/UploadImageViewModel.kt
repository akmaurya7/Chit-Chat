package com.example.chitchat.View.ViewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UploadImageViewModel(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel()
{
    private val storageReference = Firebase.storage.reference

    private suspend fun uploadImageToStorage(uri: Uri): String? {
        return try {
            val imageRef = storageReference.child("images/${uri.lastPathSegment}")
            val uploadTask = imageRef.putFile(uri).await()
            imageRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private suspend fun saveImageUrlToFirestore(email: String, imageUrl: String) {
        val userDocRef = db.collection("users").document(email)
        userDocRef.update("imageUri", imageUrl).await()
    }

    fun uploadAndSaveImage(uri: Uri) {
        viewModelScope.launch {
            val imageUrl = uploadImageToStorage(uri)
            val email = auth.currentUser?.email
            if (imageUrl != null) {
                if (email != null) {
                    saveImageUrlToFirestore(email, imageUrl)
                }
            }
        }
    }
}
