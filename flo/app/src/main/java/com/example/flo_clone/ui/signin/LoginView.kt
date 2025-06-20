package com.example.flo_clone.ui.signin

interface LoginView {
    fun onLoginSuccess(message: String)
    fun onLoginFailure(message: String)
}