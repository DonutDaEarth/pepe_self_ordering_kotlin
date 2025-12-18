package com.example.pepeselforderingapp.data.model

data class RegisterRequest(
    val email: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class User(
    val id: Int,
    val email: String,
    val created_at: String? = null,
    val updated_at: String? = null
)

data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val result: User
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val token: String,
    val user: User? = null  // Make user optional since API might not return it
)
