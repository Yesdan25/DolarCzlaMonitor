package com.yesdan.dolarczlamonitor.data.remote

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.tasks.await

class RemoteConfigRepository {
    private val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
    private val TAG = "RemoteConfigRepository"

    init {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.setDefaultsAsync(mapOf(
            "is_app_enabled" to true,
            "ad_image_url" to "",
            "ad_link_url" to "",
            "ad_target_city" to "Todas"
        ))
    }

    suspend fun isAppEnabled(): Boolean {
        return try {
            remoteConfig.fetchAndActivate().await()
            remoteConfig.getBoolean("is_app_enabled")
        } catch (e: Exception) {
            Log.e(TAG, "Error obteniendo Remote Config: ${e.message}", e)
            true
        }
    }

    suspend fun getAdImageUrl(): String {
        return try {
            remoteConfig.fetchAndActivate().await()
            remoteConfig.getString("ad_image_url")
        } catch (e: Exception) {
            Log.e(TAG, "Error obteniendo ad_image_url: ${e.message}", e)
            ""
        }
    }

    suspend fun getAdLinkUrl(): String {
        return try {
            remoteConfig.fetchAndActivate().await()
            remoteConfig.getString("ad_link_url")
        } catch (e: Exception) {
            Log.e(TAG, "Error obteniendo ad_link_url: ${e.message}", e)
            ""
        }
    }

    suspend fun getAdTargetCity(): String {
        return try {
            remoteConfig.fetchAndActivate().await()
            remoteConfig.getString("ad_target_city")
        } catch (e: Exception) {
            Log.e(TAG, "Error obteniendo ad_target_city: ${e.message}", e)
            "Todas"
        }
    }
}
