package com.practicum.playlistmaker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.domain.api.MediaPlayerInteractor

class AudioPlayerViewModelFactory(
    private val mediaPlayerInteractor: MediaPlayerInteractor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AudioPlayerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AudioPlayerViewModel(mediaPlayerInteractor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}