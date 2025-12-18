package com.example.pepeselforderingapp.data.repository

import android.content.Context
import com.example.pepeselforderingapp.data.api.RetrofitClient
import com.example.pepeselforderingapp.data.model.OutletMenusResponse
import com.example.pepeselforderingapp.data.storage.AuthDataStore
import kotlinx.coroutines.flow.first

class MenuRepository(private val context: Context) {
    private val apiService = RetrofitClient.apiService
    private val authDataStore = AuthDataStore(context)

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
