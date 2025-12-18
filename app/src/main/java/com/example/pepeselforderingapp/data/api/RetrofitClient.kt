package com.example.pepeselforderingapp.data.api

import android.util.Log
import okhttp3.Dns
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

object RetrofitClient {
    // Using IP address since emulator DNS can't resolve the hostname
    private const val BASE_URL = "https://pepe.codedoc.cloud/"
    private const val SERVER_IP = "172.67.161.82"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Custom DNS resolver that uses the known IP address
    private val customDns = object : Dns {
        override fun lookup(hostname: String): List<InetAddress> {
            return try {
                when (hostname) {
                    "pepe.codedoc.cloud" -> {
                        // Use the known IP address for the API server
                        Log.d("RetrofitClient", "Using hardcoded IP for $hostname: $SERVER_IP")
                        listOf(InetAddress.getByName(SERVER_IP))
                    }
                    else -> {
                        // For other hosts, use system DNS
                        Dns.SYSTEM.lookup(hostname)
                    }
                }
            } catch (e: UnknownHostException) {
                Log.e("RetrofitClient", "DNS lookup failed for $hostname: ${e.message}")
                emptyList()
            }
        }
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .dns(customDns)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
