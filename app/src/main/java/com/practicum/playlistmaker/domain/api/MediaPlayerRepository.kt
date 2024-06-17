package com.practicum.playlistmaker.domain.api

interface MediaPlayerRepository {
    fun prepareMediaPlayer(url: String)
    fun startMediaPlayer()
    fun pauseMediaPlayer()
    fun releaseMediaPlayer()
    fun getCurrentPosition(): Int
    fun isPlaying(): Boolean
    fun setOnCompletionListener(listener: () -> Unit)
}