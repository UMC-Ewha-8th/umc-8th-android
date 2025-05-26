package com.example.flo_clone

interface SignupView {
    fun onSignupSuccess(message: String)
    fun onSignupFailure(message: String)
}