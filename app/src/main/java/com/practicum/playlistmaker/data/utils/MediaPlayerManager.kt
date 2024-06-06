package com.practicum.playlistmaker.data.utils

interface MediaPlayerManager {
    fun prepareMediaPlayer(url: String, onPrepared: () -> Unit, onCompletion: () -> Unit)
    fun play()
    fun pause()
    fun release()
    fun isPlaying(): Boolean
    fun getCurrentPosition(): Int
}