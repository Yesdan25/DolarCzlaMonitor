package com.yesdan.dolarczlamonitor.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context) {
    companion object {
        private val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        private val SHOW_EURO = booleanPreferencesKey("show_euro")
        private val USER_CITY = stringPreferencesKey("user_city")
    }

    val isDarkMode: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_DARK_MODE] ?: false
    }

    val showEuro: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[SHOW_EURO] ?: false
    }

    val userCity: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_CITY]
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_DARK_MODE] = enabled
        }
    }

    suspend fun setShowEuro(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[SHOW_EURO] = enabled
        }
    }

    suspend fun setUserCity(city: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_CITY] = city
        }
    }
}
