package com.example.chitchat.View.ViewModel


import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chitchat.Model.EmailAuthState
import com.example.chitchat.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EmailAuthViewModel(private val auth: FirebaseAuth) : ViewModel() {
    private val _emailAuthState = MutableLiveData<EmailAuthState>()
    val emailAuthState: LiveData<EmailAuthState> = _emailAuthState

    private val _emailUserState = MutableStateFlow(User())
    val emailUserState : StateFlow<User> = _emailUserState

    fun updateUserState(userId: String) {
        _emailAuthState.value = EmailAuthState.Authenticated(userId)
    }

    fun getCurrentUser(db:FirebaseFirestore) {
        viewModelScope.launch {
            if (auth.currentUser == null) {
                _emailUserState.value = User()
            } else {
                val email = auth.currentUser!!.email
                if (email != null) {
                    val docRef = db.collection("users").document(email)
                    docRef.get()
                        .addOnSuccessListener { document ->
                            if (document != null && document.exists()) {
                                val name = document.getString("name") ?: ""
                                val imageUriString = document.getString("imageUri") ?: ""
                                val imageUri =
                                    if (imageUriString.isEmpty()) Uri.EMPTY else Uri.parse(
                                        imageUriString
                                    )
                                _emailUserState.value =
                                    User(email = email, name = name, imageUri = imageUri.toString())
                            } else {
                                _emailUserState.value = User(email = email)
                            }
                        }
                        .addOnFailureListener { e ->
                            // Handle the error, for example, by logging it
                        }
                }
            }
        }
    }

        fun emailLogin(email: String, password: String, context: Context) {
            if (email.isEmpty() || password.isEmpty()) {
                _emailAuthState.value = EmailAuthState.Error("Email or Password can't be empty")
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "signInWithEmail:success")
                            val userId = auth.currentUser?.uid ?: ""
                            _emailAuthState.value = EmailAuthState.Authenticated(userId)
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            }
        }

        fun emailSignUp(
            email: String,
            password: String,
            db: FirebaseFirestore,
            name: String,
            imageUri: String,
            context: Context
        ) {
            if (email.isEmpty() || password.isEmpty()) {
                _emailAuthState.value = EmailAuthState.Error("Email or Password can't be empty")
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "createUserWithEmail:success")
                            val userId = auth.currentUser?.uid ?: ""
                            _emailAuthState.value = EmailAuthState.Authenticated(userId)
                            saveUser(db, User(email, password, name, imageUri ), context)
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            }
        }

        fun signOut() {
            auth.signOut()
            _emailAuthState.value = EmailAuthState.Unauthenticated
        }

        fun saveUser(db: FirebaseFirestore, user: User, context: Context) {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                val userDocRef = db.collection("users").document(user.email)

                userDocRef.set(user)
                    .addOnSuccessListener { Log.d(TAG, "Document added successfully") }
                    .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
            } else {
                Toast.makeText(context, "Please login", Toast.LENGTH_SHORT).show()
            }
        }

}
