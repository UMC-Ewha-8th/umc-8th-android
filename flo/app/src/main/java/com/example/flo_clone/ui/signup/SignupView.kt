package com.example.flo_clone.ui.signup

interface SignupView {
    fun onSignupSuccess(message: String)
    fun onSignupFailure(message: String)
}