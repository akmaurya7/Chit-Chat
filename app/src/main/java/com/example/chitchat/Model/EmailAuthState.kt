package com.example.chitchat.Model


sealed class EmailAuthState {
    data object Unauthenticated : EmailAuthState()
    data object Loading : EmailAuthState()
    data class Authenticated(val user: String) : EmailAuthState()
    data class Error(val message: String) : EmailAuthState()
}
