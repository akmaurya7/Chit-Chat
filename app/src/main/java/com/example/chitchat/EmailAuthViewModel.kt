package com.example.chitchat


import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EmailAuthViewModel(private val auth: FirebaseAuth) : ViewModel() {
    private val _emailAuthState = MutableLiveData<EmailAuthState>()
    val emailAuthState: LiveData<EmailAuthState> = _emailAuthState


    fun updateUserState(userId: String) {
        _emailAuthState.value = EmailAuthState.Authenticated(userId)
    }

    fun getCurrentUser(): User {
        return if (auth.currentUser == null) {
            User(
                email = "",
                password = "",
                name = ""
            )
        } else {
            val email = auth.currentUser?.email ?: ""
            val name = auth.currentUser?.displayName ?: ""
            val password = ""
            User(
                email = email,
                password = password,
                name = name
            )
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
                        Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    fun emailSignUp(email: String, password: String, db: FirebaseFirestore, name: String, context: Context) {
        if (email.isEmpty() || password.isEmpty()) {
            _emailAuthState.value = EmailAuthState.Error("Email or Password can't be empty")
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        val userId = auth.currentUser?.uid ?: ""
                        _emailAuthState.value = EmailAuthState.Authenticated(userId)
                        saveUser(db, User(email, password, name), context)
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
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
            val userId = currentUser.uid
            val userDocRef = db.collection("users").document(user.email)

            userDocRef.set(user)
                .addOnSuccessListener { Log.d(TAG, "Document added successfully") }
                .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
        } else {
            Toast.makeText(context, "Please login", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val TAG = "EmailAuthViewModel"
    }
}
