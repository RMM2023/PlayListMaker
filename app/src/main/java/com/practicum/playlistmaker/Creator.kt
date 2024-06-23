package com.practicum.playlistmaker

import com.practicum.playlistmaker.data.remote.api.ITunesApiService
import com.practicum.playlistmaker.data.repository.MediaPlayerRepositoryImpl
import com.practicum.playlistmaker.data.repository.TrackRepositoryImpl
import com.practicum.playlistmaker.domain.api.MediaPlayerInteractor
import com.practicum.playlistmaker.domain.api.MediaPlayerRepository
import com.practicum.playlistmaker.domain.repository.TrackRepository
import com.practicum.playlistmaker.domain.usecase.MediaPlayerInteractorImpl
import com.practicum.playlistmaker.domain.usecase.SearchTracksUseCase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import android.content.Context
import com.practicum.playlistmaker.data.ResourceManager
import com.practicum.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.data.repository.SettingsRepositoryImpl
import com.practicum.playlistmaker.domain.repository.SearchHistoryRepository
import com.practicum.playlistmaker.domain.repository.SettingsRepository
import com.practicum.playlistmaker.domain.usecase.*
import com.practicum.playlistmaker.presentation.ui.activity.SearchActivity

object Creator {
    private fun provideMediaPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl()
    }

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        val repository = provideMediaPlayerRepository()
        return MediaPlayerInteractorImpl(repository)
    }

    private fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun provideApiService(retrofit: Retrofit): ITunesApiService {
        return retrofit.create(ITunesApiService::class.java)
    }

    private fun provideTrackRepository(apiService: ITunesApiService): TrackRepository {
        return TrackRepositoryImpl(apiService)
    }

    fun provideSearchTracksUseCase(): SearchTracksUseCase {
        val retrofit = provideRetrofit()
        val apiService = provideApiService(retrofit)
        val trackRepository = provideTrackRepository(apiService)
        return SearchTracksUseCase(trackRepository)
    }

    private fun provideSettingsRepository(context: Context): SettingsRepository {
        val sharedPreferences = context.getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)
        val resourceManager = ResourceManager(context)
        return SettingsRepositoryImpl(sharedPreferences, resourceManager)
    }

    fun provideGetThemeSettingsUseCase(context: Context): GetThemeSettingsUseCase {
        return GetThemeSettingsUseCase(provideSettingsRepository(context))
    }

    fun provideSetThemeSettingsUseCase(context: Context): SetThemeSettingsUseCase {
        return SetThemeSettingsUseCase(provideSettingsRepository(context))
    }

    fun provideGetShareAppLinkUseCase(context: Context): GetShareAppLinkUseCase {
        return GetShareAppLinkUseCase(provideSettingsRepository(context))
    }

    fun provideGetSupportEmailDataUseCase(context: Context): GetSupportEmailDataUseCase {
        return GetSupportEmailDataUseCase(provideSettingsRepository(context))
    }

    fun provideGetUserAgreementLinkUseCase(context: Context): GetUserAgreementLinkUseCase {
        return GetUserAgreementLinkUseCase(provideSettingsRepository(context))
    }

    fun provideGetSearchHistoryUseCase(context: Context): GetSearchHistoryUseCase {
        return GetSearchHistoryUseCase(provideSearchHistoryRepository(context))
    }

    fun provideAddTrackToHistoryUseCase(context: Context): AddTrackToHistoryUseCase {
        return AddTrackToHistoryUseCase(provideSearchHistoryRepository(context))
    }

    fun provideClearSearchHistoryUseCase(context: Context): ClearSearchHistoryUseCase {
        return ClearSearchHistoryUseCase(provideSearchHistoryRepository(context))
    }

    private fun provideSearchHistoryRepository(context: Context): SearchHistoryRepository {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return SearchHistoryRepositoryImpl(sharedPreferences)
    }
}