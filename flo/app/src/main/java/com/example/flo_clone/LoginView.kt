package com.example.flo_clone

interface LoginView {
    fun onLoginSuccess(message: String)
    fun onLoginFailure(message: String)
}