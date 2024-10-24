package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.google.gson.Gson
import com.practicum.playlistmaker.data.ResourceManager
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.db.PlaylistsDatabase
import com.practicum.playlistmaker.data.db.PlaylistsDbConverter
import com.practicum.playlistmaker.data.db.TracksToPlaylistConverter
import com.practicum.playlistmaker.data.remote.api.ITunesApiService
import com.practicum.playlistmaker.data.repository.FavoriteTracksRepositoryImpl
import com.practicum.playlistmaker.data.repository.MediaPlayerRepositoryImpl
import com.practicum.playlistmaker.data.repository.PlaylistsRepositoryImpl
import com.practicum.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.data.repository.SettingsRepositoryImpl
import com.practicum.playlistmaker.data.repository.TrackRepositoryImpl
import com.practicum.playlistmaker.domain.api.MediaPlayerInteractor
import com.practicum.playlistmaker.domain.api.MediaPlayerRepository
import com.practicum.playlistmaker.domain.interactor.FavoriteTracksInteractor
import com.practicum.playlistmaker.domain.interactor.PlaylistsInteractor
import com.practicum.playlistmaker.domain.repository.FavoriteTracksRepository
import com.practicum.playlistmaker.domain.repository.PlaylistsRepository
import com.practicum.playlistmaker.domain.repository.SearchHistoryRepository
import com.practicum.playlistmaker.domain.repository.SettingsRepository
import com.practicum.playlistmaker.domain.repository.TrackRepository
import com.practicum.playlistmaker.domain.usecase.*
import com.practicum.playlistmaker.presentation.viewmodel.AudioPlayerViewModel
import com.practicum.playlistmaker.presentation.viewmodel.CurrentPlaylistViewModel
import com.practicum.playlistmaker.presentation.viewmodel.FavoriteViewModel
import com.practicum.playlistmaker.presentation.viewmodel.MediaViewModel
import com.practicum.playlistmaker.presentation.viewmodel.ModifyPlaylistViewModel
import com.practicum.playlistmaker.presentation.viewmodel.NewPlaylistViewModel
import com.practicum.playlistmaker.presentation.viewmodel.PlayListViewModel
import com.practicum.playlistmaker.presentation.viewmodel.SearchViewModel
import com.practicum.playlistmaker.presentation.viewmodel.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single { androidContext().getSharedPreferences("settings_prefs", Context.MODE_PRIVATE) }
    single { ResourceManager(androidContext()) }
    single<ITunesApiService> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApiService::class.java)
    }
    single { Gson() }
    factory { MediaPlayer() }
    single<TrackRepository> { TrackRepositoryImpl(get()) }
    single<SearchHistoryRepository> { SearchHistoryRepositoryImpl(get(), get()) }
    single<SettingsRepository> { SettingsRepositoryImpl(get(), get()) }
    single{
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "play-list-maker-db")
            .fallbackToDestructiveMigration()
            .build()
    }
    single {
        Room.databaseBuilder(androidContext(), PlaylistsDatabase::class.java, "playlists-db")
            .fallbackToDestructiveMigration()
            .build()
    }
    factory<MediaPlayerRepository> { MediaPlayerRepositoryImpl(get()) }
    single<PlaylistsRepository> { PlaylistsRepositoryImpl(get(),get(),get())}

}


val domainModule = module {
    factory { SearchTracksUseCase(get()) }
    factory { GetSearchHistoryUseCase(get()) }
    factory { AddTrackToHistoryUseCase(get()) }
    factory { ClearSearchHistoryUseCase(get()) }
    factory { GetThemeSettingsUseCase(get()) }
    factory { SetThemeSettingsUseCase(get()) }
    factory { GetShareAppLinkUseCase(get()) }
    factory { GetSupportEmailDataUseCase(get()) }
    factory { GetUserAgreementLinkUseCase(get()) }
    factory { FavoriteTracksInteractor(get()) }
    factory<MediaPlayerInteractor> { MediaPlayerInteractorImpl(get()) }
    factory<FavoriteTracksRepository> { FavoriteTracksRepositoryImpl(get()) }
    factory { PlaylistsInteractor(get()) }
    factory { PlaylistsDbConverter() }
    factory { TracksToPlaylistConverter() }
}

val viewModelModule = module {
    viewModel { SearchViewModel(get(), get(), get(), get()) }
    viewModel { SettingsViewModel(get(), get(), get(), get(), get()) }
    viewModel { AudioPlayerViewModel(get(), get(), get()) }
    viewModel { FavoriteViewModel(get()) }
    viewModel { MediaViewModel()}
    viewModel { PlayListViewModel(get())}
    viewModel { NewPlaylistViewModel(get()) }
    viewModel { CurrentPlaylistViewModel(get()) }
    viewModel { ModifyPlaylistViewModel(get()) }
}