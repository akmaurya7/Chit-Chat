package com.example.chitchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController

import com.example.chitchat.ui.theme.ChitChatTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize FirebaseAuth
        val auth = FirebaseAuth.getInstance()

        // Initialize the ViewModelFactory
        val factory = EmailAuthViewModelFactory(auth)

        // Use the ViewModelProvider to get the EmailAuthViewModel
        val emailAuthViewModel: EmailAuthViewModel by viewModels { factory }

        val db = FirebaseFirestore.getInstance()

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            ChitChatTheme {
                NavGraph(navController,emailAuthViewModel,db)
            }
        }
    }
}
