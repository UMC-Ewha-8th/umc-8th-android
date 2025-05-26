package com.example.flo_clone

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthRetrofitInterface {
    @POST("/join")
    fun signUp(@Body user: SignupRequest): Call<AuthResponse>

    @POST("/login")
    fun login(@Body loginRequest:LoginRequest): Call<AuthResponse>
}
