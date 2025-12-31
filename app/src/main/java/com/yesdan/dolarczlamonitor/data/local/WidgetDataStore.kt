package com.yesdan.dolarczlamonitor.data.local

import android.content.Context
import android.content.SharedPreferences

object WidgetDataStore {
    private const val PREFS_NAME = "widget_prefs"
    private const val KEY_BCV_PRICE = "bcv_price"
    private const val KEY_BINANCE_PRICE = "binance_price"
    private const val KEY_SPREAD_PERCENTAGE = "spread_percentage"
    private const val KEY_LAST_UPDATE = "last_update"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveBcvPrice(context: Context, price: Double) {
        getSharedPreferences(context).edit()
            .putFloat(KEY_BCV_PRICE, price.toFloat())
            .apply()
    }

    fun getBcvPrice(context: Context): Double {
        return getSharedPreferences(context).getFloat(KEY_BCV_PRICE, 0f).toDouble()
    }

    fun saveBinancePrice(context: Context, price: Double) {
        getSharedPreferences(context).edit()
            .putFloat(KEY_BINANCE_PRICE, price.toFloat())
            .apply()
    }

    fun getBinancePrice(context: Context): Double {
        return getSharedPreferences(context).getFloat(KEY_BINANCE_PRICE, 0f).toDouble()
    }

    fun saveSpreadPercentage(context: Context, percentage: Double) {
        getSharedPreferences(context).edit()
            .putFloat(KEY_SPREAD_PERCENTAGE, percentage.toFloat())
            .apply()
    }

    fun getSpreadPercentage(context: Context): Double {
        return getSharedPreferences(context).getFloat(KEY_SPREAD_PERCENTAGE, 0f).toDouble()
    }

    fun saveLastUpdate(context: Context, timestamp: Long) {
        getSharedPreferences(context).edit()
            .putLong(KEY_LAST_UPDATE, timestamp)
            .apply()
    }

    fun getLastUpdate(context: Context): Long {
        return getSharedPreferences(context).getLong(KEY_LAST_UPDATE, 0L)
    }

    fun clear(context: Context) {
        getSharedPreferences(context).edit().clear().apply()
    }
}
