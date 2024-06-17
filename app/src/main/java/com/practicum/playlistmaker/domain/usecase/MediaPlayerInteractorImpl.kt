package com.practicum.playlistmaker.domain.usecase

import com.practicum.playlistmaker.domain.api.MediaPlayerInteractor
import com.practicum.playlistmaker.domain.api.MediaPlayerRepository
import com.practicum.playlistmaker.domain.model.MediaPlayerState

class MediaPlayerInteractorImpl(private val repository: MediaPlayerRepository) : MediaPlayerInteractor {

    private var mediaPlayerState = MediaPlayerState.IDLE

    override fun initMediaPlayer(url: String) {
        repository.prepareMediaPlayer(url)
        mediaPlayerState = MediaPlayerState.INITIALIZED
    }

    override fun playPauseMediaPlayer() {
        if (repository.isPlaying()) {
            repository.pauseMediaPlayer()
            mediaPlayerState = MediaPlayerState.PAUSED
        } else {
            repository.startMediaPlayer()
            mediaPlayerState = MediaPlayerState.STARTED
        }
    }

    override fun releaseMediaPlayer() {
        repository.releaseMediaPlayer()
        mediaPlayerState = MediaPlayerState.END
    }

    override fun getCurrentPosition(): Int {
        return repository.getCurrentPosition()
    }

    override fun isPlaying(): Boolean {
        return repository.isPlaying()
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        repository.setOnCompletionListener {
            listener()
            mediaPlayerState = MediaPlayerState.COMPLETED
        }
    }
}