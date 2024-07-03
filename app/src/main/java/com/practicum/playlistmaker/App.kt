package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val CURRENT_TRACK = "current_track"
const val THEME_KEY = "theme_key"
const val PREF_NAME = "pref_name"

class App : Application() {
    var isDarkTheme: Boolean = false

    override fun onCreate() {
        super.onCreate()

        val sPref = getSharedPreferences("settings_prefs", MODE_PRIVATE)
        isDarkTheme = sPref.getBoolean(THEME_KEY, false)
        themeToggle(isDarkTheme)
    }

    fun themeToggle(isDarkTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            when (isDarkTheme) {
                true -> AppCompatDelegate.MODE_NIGHT_YES
                false -> AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}