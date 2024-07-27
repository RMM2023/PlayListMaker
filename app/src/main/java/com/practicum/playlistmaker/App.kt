package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.di.dataModule
import com.practicum.playlistmaker.di.domainModule
import com.practicum.playlistmaker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

const val CURRENT_TRACK = "current_track"
const val THEME_KEY = "theme_key"
const val PREF_NAME = "pref_name"

class App : Application() {
    var isDarkTheme: Boolean = false

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(dataModule, domainModule, viewModelModule)
        }

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