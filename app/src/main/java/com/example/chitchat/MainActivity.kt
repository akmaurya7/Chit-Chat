package com.example.chitchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.Navigation.findNavController
import androidx.navigation.compose.rememberNavController
import com.example.chitchat.View.Navigation.NavGraph
import com.example.chitchat.View.Navigation.Routes
import com.example.chitchat.View.ViewModel.EmailAuthViewModel
import com.example.chitchat.View.ViewModel.EmailAuthViewModelFactory
import com.example.chitchat.View.ViewModel.UploadImageViewModel
import com.example.chitchat.View.ViewModel.UploadImageViewModelFactory

import com.example.chitchat.ui.theme.ChitChatTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {

    // Initialize FirebaseAuth
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = FirebaseFirestore.getInstance()

        val emailAuthFactory = EmailAuthViewModelFactory(auth)
        val emailAuthViewModel: EmailAuthViewModel by viewModels { emailAuthFactory }

        val uploadImageFactory = UploadImageViewModelFactory(auth,db)
        val uploadImageViewModel: UploadImageViewModel by viewModels {uploadImageFactory}


        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            ChitChatTheme {
                NavGraph(navController,emailAuthViewModel,uploadImageViewModel,db,auth)
            }
        }
    }

//    public override fun onStart() {
//        super.onStart()
//        val currentUser = auth.currentUser
//        if (currentUser != null) {
//            val navController = findNavController(R.id.nav_host_fragment)
//            navController.navigate(Routes.HomeScreen)
//        }
//    }
}
