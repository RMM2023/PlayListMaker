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
}