package com.yesdan.dolarczlamonitor.utils

import android.content.Context
import android.os.Build
import com.google.firebase.analytics.FirebaseAnalytics

object AnalyticsHelper {
    fun setUserCity(context: Context, city: String) {
        val analytics = FirebaseAnalytics.getInstance(context)
        analytics.setUserProperty("user_city", city)
    }

    fun logAppOpen(context: Context, city: String?, deviceModel: String) {
        val analytics = FirebaseAnalytics.getInstance(context)
        val bundle = android.os.Bundle().apply {
            putString("user_city", city ?: "unknown")
            putString("device_model", deviceModel)
            putString("android_version", Build.VERSION.SDK_INT.toString())
        }
        analytics.logEvent("app_open", bundle)
    }

    fun logCitySelected(context: Context, city: String) {
        val analytics = FirebaseAnalytics.getInstance(context)
        val bundle = android.os.Bundle().apply {
            putString("city", city)
        }
        analytics.logEvent("city_selected", bundle)
    }

    fun logAdClicked(context: Context, adCity: String) {
        val analytics = FirebaseAnalytics.getInstance(context)
        val bundle = android.os.Bundle().apply {
            putString("ad_city", adCity)
        }
        analytics.logEvent("ad_clicked", bundle)
    }
}
