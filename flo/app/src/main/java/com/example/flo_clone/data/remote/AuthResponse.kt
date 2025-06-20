package com.example.flo_clone.data.remote

data class AuthResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: AuthResult?
)

data class AuthResult(
    val memberId: Int?,
    val accessToken: String?,
    val createdAt: String?,
    val updatedAt: String?
)
