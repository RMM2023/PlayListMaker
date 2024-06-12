package com.practicum.playlistmaker.data.repository

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.api.MediaPlayerRepository
import com.practicum.playlistmaker.domain.model.MediaPlayerState

class MediaPlayerRepositoryImpl : MediaPlayerRepository {
    private var mediaPlayer: MediaPlayer? = null

    override fun createMediaPlayer(): MediaPlayer {
        mediaPlayer = MediaPlayer()
        return mediaPlayer!!
    }

    override fun prepareMediaPlayer(url: String) {
        mediaPlayer?.apply {
            setDataSource(url)
            prepareAsync()
        }
    }

    override fun startMediaPlayer() {
        mediaPlayer?.start()
    }

    override fun pauseMediaPlayer() {
        mediaPlayer?.pause()
    }

    override fun releaseMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }
}