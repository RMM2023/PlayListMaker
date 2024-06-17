package com.practicum.playlistmaker.data.repository

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.api.MediaPlayerRepository

class MediaPlayerRepositoryImpl : MediaPlayerRepository {
    private val mediaPlayer: MediaPlayer = MediaPlayer()

    override fun prepareMediaPlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
    }

    override fun startMediaPlayer() {
        mediaPlayer.start()
    }

    override fun pauseMediaPlayer() {
        mediaPlayer.pause()
    }

    override fun releaseMediaPlayer() {
        mediaPlayer.release()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            listener()
        }
    }
}