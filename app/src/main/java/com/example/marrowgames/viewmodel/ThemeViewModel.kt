package com.example.marrowgames.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marrowgames.ui.theme.ThemePreferenceManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(context: Context) : ViewModel() {
    val isDarkTheme: StateFlow<Boolean> = ThemePreferenceManager
        .getThemeFlow(context)
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun setTheme(context: Context, dark: Boolean) {
        viewModelScope.launch {
            ThemePreferenceManager.setTheme(context, dark)
        }
    }
}
