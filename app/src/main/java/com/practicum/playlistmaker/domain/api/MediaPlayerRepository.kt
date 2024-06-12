package com.practicum.playlistmaker.domain.api

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.model.MediaPlayerState

interface MediaPlayerRepository {
    fun createMediaPlayer(): MediaPlayer
    fun prepareMediaPlayer(url: String)
    fun startMediaPlayer()
    fun pauseMediaPlayer()
    fun releaseMediaPlayer()
    fun getCurrentPosition(): Int
    fun isPlaying(): Boolean
}