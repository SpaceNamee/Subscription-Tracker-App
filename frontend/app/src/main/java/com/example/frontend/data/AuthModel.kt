package com.example.frontend.data

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val access_token: String,
    val refresh_token: String,
    val token_type: String
)

data class RegisterRequest(
    val email: String,
    val password: String
)

data class RegisterResponse(
    val message: String,
    val success: Boolean
)

