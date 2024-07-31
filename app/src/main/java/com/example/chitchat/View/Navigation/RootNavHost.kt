package com.example.chitchat.View.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.chitchat.View.Screen.EmailSignInScreen
import com.example.chitchat.View.Screen.EmailSignUpScreen
import com.example.chitchat.View.Screen.HomeScreen
import com.example.chitchat.View.ViewModel.EmailAuthViewModel
import com.example.chitchat.View.ViewModel.UploadImageViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun NavGraph(
    navController: NavHostController,
    emailAuthViewModel: EmailAuthViewModel,
    uploadImageViewModel: UploadImageViewModel,
    db:FirebaseFirestore,
    auth:FirebaseAuth)
{
    NavHost(
        navController = navController,
        startDestination = if (auth.currentUser!= null) Routes.HomeScreen else Routes.EmailSignInScreen
    ) {
        composable(Routes.EmailSignInScreen) {
            EmailSignInScreen(navController,emailAuthViewModel)
        }
        composable(Routes.EmailSignUpScreen) {
            EmailSignUpScreen(navController,emailAuthViewModel,uploadImageViewModel,db)
        }
        composable(Routes.HomeScreen) {
            LaunchedEffect(Unit) {
                emailAuthViewModel.getCurrentUser(db)
            }
            HomeScreen(navController,emailAuthViewModel,db)
        }
    }
}
