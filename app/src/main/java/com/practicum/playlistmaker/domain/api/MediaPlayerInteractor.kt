package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.model.MediaPlayerState

interface MediaPlayerInteractor {
    fun initMediaPlayer(url: String)
    fun playPauseMediaPlayer()
    fun releaseMediaPlayer()
    fun getCurrentPosition(): Int
    fun isPlaying(): Boolean
    fun setOnCompletionListener(listener: () -> Unit)
}