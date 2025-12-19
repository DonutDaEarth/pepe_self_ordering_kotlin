package com.example.pepeselforderingapp

import android.app.Application
import android.util.Log
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import okhttp3.Dns
import okhttp3.OkHttpClient
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class PepeApplication : Application(), ImageLoaderFactory {

    // Custom DNS resolver that bypasses system DNS issues
    private val customDns = object : Dns {
        override fun lookup(hostname: String): List<InetAddress> {
            Log.d("PepeApplication", "DNS lookup for: $hostname")
            return try {
                when (hostname) {
                    "pepe.codedoc.cloud" -> {
                        // Use hardcoded IP for API server
                        Log.d("PepeApplication", "Using hardcoded IP for API server")
                        listOf(InetAddress.getByName("172.67.161.82"))
                    }
                    "storage.googleapis.com" -> {
                        // Google Cloud Storage has multiple IPs, we need to resolve them
                        // Try multiple methods to resolve the hostname
                        Log.d("PepeApplication", "Attempting to resolve storage.googleapis.com")

                        // Method 1: Try InetAddress.getAllByName (should work if DNS is working)
                        try {
                            val addresses = InetAddress.getAllByName(hostname).toList()
                            if (addresses.isNotEmpty()) {
                                Log.d("PepeApplication", "Resolved ${addresses.size} addresses for $hostname")
                                return addresses
                            }
                        } catch (e: Exception) {
                            Log.w("PepeApplication", "InetAddress.getAllByName failed: ${e.message}")
                        }

                        // Method 2: Try using specific known IPs for storage.googleapis.com
                        // These are some of the common IPs for Google Cloud Storage
                        Log.d("PepeApplication", "Using known Google Cloud Storage IPs")
                        listOf(
                            InetAddress.getByName("142.250.185.207"),  // Common GCS IP
                            InetAddress.getByName("172.217.194.207"),  // Alternate GCS IP
                            InetAddress.getByName("216.58.211.207")    // Alternate GCS IP
                        )
                    }
                    else -> {
                        // For other hosts, use system DNS
                        Log.d("PepeApplication", "Using system DNS for $hostname")
                        Dns.SYSTEM.lookup(hostname)
                    }
                }
            } catch (e: UnknownHostException) {
                Log.e("PepeApplication", "DNS lookup completely failed for $hostname: ${e.message}")
                emptyList()
            }
        }
    }

    override fun newImageLoader(): ImageLoader {
        Log.d("PepeApplication", "Initializing ImageLoader with custom DNS")
        return ImageLoader.Builder(this)
            .okHttpClient {
                OkHttpClient.Builder()
                    .dns(customDns)  // Add custom DNS resolver
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build()
            }
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.02)
                    .build()
            }
            .respectCacheHeaders(false)
            .crossfade(true)
            .build()
    }
}
