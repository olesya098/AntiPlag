package com.hfad.antiplag.viewModel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ThemeViewModel : ViewModel() {
    
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()
    
    private var sharedPreferences: SharedPreferences? = null
    
    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        loadThemePreference()
    }
    
    private fun loadThemePreference() {
        val savedTheme = sharedPreferences?.getBoolean("is_dark_theme", false) ?: false
        _isDarkTheme.value = savedTheme
    }
    
    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
        saveThemePreference()
    }
    
    fun setTheme(isDark: Boolean) {
        _isDarkTheme.value = isDark
        saveThemePreference()
    }
    
    private fun saveThemePreference() {
        viewModelScope.launch {
            sharedPreferences?.edit()?.apply {
                putBoolean("is_dark_theme", _isDarkTheme.value)
                apply()
            }
        }
    }
    
    fun getCurrentTheme(): Boolean = _isDarkTheme.value
}
