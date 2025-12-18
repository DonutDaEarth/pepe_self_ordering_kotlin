package com.example.pepeselforderingapp.data.repository

import android.util.Base64
import com.example.pepeselforderingapp.data.api.RetrofitClient
import com.example.pepeselforderingapp.data.model.LoginRequest
import com.example.pepeselforderingapp.data.model.LoginResponse
import com.example.pepeselforderingapp.data.model.RegisterRequest
import com.example.pepeselforderingapp.data.storage.AuthDataStore
import org.json.JSONObject

sealed class AuthResult<out T> {
    data class Success<T>(val data: T) : AuthResult<T>()
    data class Error(val message: String) : AuthResult<Nothing>()
}

class AuthRepository(private val authDataStore: AuthDataStore) {

    // Helper function to decode JWT and extract user info
    private fun decodeJWT(token: String): Pair<Int, String>? {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return null

            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_WRAP))
            val json = JSONObject(payload)
            val userId = json.getInt("userId")

            // We don't have email in JWT, so return empty string
            Pair(userId, "")
        } catch (e: Exception) {
            null
        }
    }

    suspend fun register(email: String, password: String): AuthResult<LoginResponse> {
        return try {
            // First, register the user
            val registerResponse = RetrofitClient.apiService.register(
                RegisterRequest(email, password)
            )

            if (registerResponse.isSuccessful && registerResponse.body()?.success == true) {
                // Automatically log in after successful registration
                login(email, password)
            } else {
                val errorMsg = registerResponse.body()?.message
                    ?: registerResponse.errorBody()?.string()
                    ?: "Registration failed"
                AuthResult.Error(errorMsg)
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Network error occurred")
        }
    }

    suspend fun login(email: String, password: String): AuthResult<LoginResponse> {
        return try {
            val response = RetrofitClient.apiService.login(
                LoginRequest(email, password)
            )

            if (response.isSuccessful && response.body()?.success == true) {
                val loginResponse = response.body()!!

                // Decode JWT to get user ID
                val userInfo = decodeJWT(loginResponse.token)
                val userId = userInfo?.first ?: 0

                // Save auth data to DataStore
                authDataStore.saveAuthData(
                    token = loginResponse.token,
                    userId = userId,
                    email = email // Use the email from login request
                )

                AuthResult.Success(loginResponse)
            } else {
                val errorMsg = response.body()?.message
                    ?: response.errorBody()?.string()
                    ?: "Login failed"
                AuthResult.Error(errorMsg)
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Network error occurred")
        }
    }

    suspend fun logout() {
        authDataStore.clearAuthData()
    }

    fun getToken() = authDataStore.token
    fun getUserId() = authDataStore.userId
    fun getUserEmail() = authDataStore.userEmail
}
