package com.example.pepeselforderingapp.data.api

import com.example.pepeselforderingapp.data.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("/users/register/customer")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("/users/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("/outlet-menus/outlet/{outlet_id}")
    suspend fun getOutletMenus(
        @Path("outlet_id") outletId: String,
        @Header("Authorization") authorization: String
    ): Response<OutletMenusResponse>

    @GET("/outlet-menus/search")
    suspend fun searchMenus(
        @Query("outlet_id") outletId: String,
        @Query("keyword") keyword: String,
        @Header("Authorization") authorization: String
    ): Response<SearchMenuResponse>

    @POST("/orders")
    suspend fun createOrder(
        @Header("Authorization") authorization: String,
        @Body request: CreateOrderRequest
    ): Response<CreateOrderResponse>

    @GET("/orders/track/{uid}")
    suspend fun trackOrder(
        @Path("uid") uid: String,
        @Header("Authorization") authorization: String
    ): Response<TrackOrderResponse>
}
