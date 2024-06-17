package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.presentation.ui.activity.IS_DARK_THEME
import com.practicum.playlistmaker.presentation.ui.activity.PREF_STATUS
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val CURRENT_TRACK = "current_track"

class App : Application() {
    var isDarkTheme: Boolean = false

    override fun onCreate() {
        super.onCreate()

        val sPref = getSharedPreferences(IS_DARK_THEME, MODE_PRIVATE)
        isDarkTheme = sPref.getBoolean(PREF_STATUS, false)
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