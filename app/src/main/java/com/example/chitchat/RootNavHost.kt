package com.example.chitchat

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun NavGraph(navController: NavHostController, emailAuthViewModel: EmailAuthViewModel, db:FirebaseFirestore) {
    NavHost(
        navController = navController,
        startDestination = Routes.EmailSignInScreen
    ) {
        composable(Routes.EmailSignInScreen) {
            EmailSignInScreen(navController,emailAuthViewModel)
        }
        composable(Routes.EmailSignUpScreen) {
            EmailSignUpScreen(navController,emailAuthViewModel,db)
        }
        composable(Routes.HomeScreen) {
            HomeScreen(navController)
        }
    }
}
