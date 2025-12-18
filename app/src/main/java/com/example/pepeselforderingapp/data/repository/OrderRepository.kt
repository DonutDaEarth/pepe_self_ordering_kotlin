package com.example.pepeselforderingapp.data.repository

import android.content.Context
import com.example.pepeselforderingapp.data.api.RetrofitClient
import com.example.pepeselforderingapp.data.model.*
import com.example.pepeselforderingapp.data.storage.AuthDataStore
import kotlinx.coroutines.flow.first

class OrderRepository(private val context: Context) {
    private val apiService = RetrofitClient.apiService
    private val authDataStore = AuthDataStore(context)

    suspend fun createOrder(request: CreateOrderRequest): Result<CreateOrderResponse> {
        return try {
            // Get the token from DataStore
            val token = authDataStore.token.first()

            if (token == null) {
                return Result.failure(Exception("No authentication token found"))
            }

            // Call API with Bearer token
            val response = apiService.createOrder("Bearer $token", request)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to create order: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun trackOrder(uid: String): Result<TrackOrderResponse> {
        return try {
            // Get the token from DataStore
            val token = authDataStore.token.first()

            if (token == null) {
                return Result.failure(Exception("No authentication token found"))
            }

            // Call API with Bearer token
            val response = apiService.trackOrder(uid, "Bearer $token")

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to track order: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getOutletMenus(outletId: String): Result<OutletMenusResponse> {
        return try {
            // Get the token from DataStore
            val token = authDataStore.token.first()

            if (token == null) {
                return Result.failure(Exception("No authentication token found"))
            }

            // Call API with Bearer token
            val response = apiService.getOutletMenus(outletId, "Bearer $token")

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch menus: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
