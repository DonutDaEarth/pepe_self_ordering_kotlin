package com.example.pepeselforderingapp.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

class AuthDataStore(private val context: Context) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
        private val USER_ID_KEY = intPreferencesKey("user_id")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val LAST_ORDER_UID_KEY = stringPreferencesKey("last_order_uid")
    }

    suspend fun saveAuthData(token: String, userId: Int, email: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[USER_ID_KEY] = userId
            preferences[USER_EMAIL_KEY] = email
        }
    }

    suspend fun saveLastOrderUid(uid: String) {
        context.dataStore.edit { preferences ->
            preferences[LAST_ORDER_UID_KEY] = uid
        }
    }

    suspend fun clearLastOrderUid() {
        context.dataStore.edit { preferences ->
            preferences.remove(LAST_ORDER_UID_KEY)
        }
    }

    val token: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }

    val userId: Flow<Int?> = context.dataStore.data.map { preferences ->
        preferences[USER_ID_KEY]
    }

    val userEmail: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_EMAIL_KEY]
    }

    val lastOrderUid: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[LAST_ORDER_UID_KEY]
    }

    suspend fun clearAuthData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun isLoggedIn(): Boolean {
        var isLoggedIn = false
        context.dataStore.data.map { preferences ->
            isLoggedIn = preferences[TOKEN_KEY] != null
        }
        return isLoggedIn
    }
}
