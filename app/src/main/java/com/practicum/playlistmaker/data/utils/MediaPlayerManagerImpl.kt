package com.practicum.playlistmaker.data.utils

import android.media.MediaPlayer

class MediaPlayerManagerImpl : MediaPlayerManager {
    private val mediaPlayer: MediaPlayer = MediaPlayer()

    override fun prepareMediaPlayer(url: String, onPrepared: () -> Unit, onCompletion: () -> Unit) {
        mediaPlayer.apply {
            setDataSource(url)
            setOnPreparedListener {
                onPrepared()
            }
            prepareAsync()
            setOnCompletionListener {
                onCompletion()
            }
        }
    }

    override fun play() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }
}