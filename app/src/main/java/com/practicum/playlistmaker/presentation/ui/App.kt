package com.practicum.playlistmaker.presentation.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.data.remote.api.ITunesApiService
import com.practicum.playlistmaker.data.repository.TrackRepositoryImpl
import com.practicum.playlistmaker.domain.repository.TrackRepository
import com.practicum.playlistmaker.domain.usecase.SearchTracksUseCase
import com.practicum.playlistmaker.presentation.ui.activity.IS_DARK_THEME
import com.practicum.playlistmaker.presentation.ui.activity.PREF_STATUS
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val CURRENT_TRACK = "current_track"

class App : Application() {
    lateinit var searchTracksUseCase: SearchTracksUseCase
    var isDarkTheme: Boolean = false

    override fun onCreate() {
        super.onCreate()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ITunesApiService::class.java)
        val trackRepository: TrackRepository = TrackRepositoryImpl(apiService)
        searchTracksUseCase = SearchTracksUseCase(trackRepository)

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