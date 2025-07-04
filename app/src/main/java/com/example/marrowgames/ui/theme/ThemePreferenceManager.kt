package com.example.marrowgames.ui.theme

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object ThemePreferenceManager {
    val Context.themeDataStore by preferencesDataStore(name = "theme_prefs")
    private val THEME_KEY = booleanPreferencesKey("dark_theme")

    fun getThemeFlow(context: Context): Flow<Boolean> =
        context.themeDataStore.data.map { prefs ->
            prefs[THEME_KEY] ?: false // false = light, true = dark
        }

    suspend fun setTheme(context: Context, isDark: Boolean) {
        context.themeDataStore.edit { prefs ->
            prefs[THEME_KEY] = isDark
        }
    }
}
