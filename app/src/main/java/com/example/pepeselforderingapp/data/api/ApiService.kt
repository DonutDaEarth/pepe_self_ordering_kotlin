package com.example.pepeselforderingapp.data.api

import com.example.pepeselforderingapp.data.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/users/register/customer")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("/users/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}

