package com.practicum.playlistmaker.domain.usecase

import com.practicum.playlistmaker.domain.api.MediaPlayerInteractor
import com.practicum.playlistmaker.domain.api.MediaPlayerRepository
import com.practicum.playlistmaker.domain.model.MediaPlayerState

class MediaPlayerInteractorImpl(private val repository: MediaPlayerRepository) : MediaPlayerInteractor {

    private var mediaPlayerState = MediaPlayerState.IDLE
    private var completionListener: (() -> Unit)? = null

    override fun initMediaPlayer(url: String) {
        repository.prepareMediaPlayer(url)
        mediaPlayerState = MediaPlayerState.INITIALIZED
        repository.setOnCompletionListener {
            mediaPlayerState = MediaPlayerState.COMPLETED
            completionListener?.invoke()
        }
    }

    override fun playPauseMediaPlayer() {
        when (mediaPlayerState) {
            MediaPlayerState.INITIALIZED, MediaPlayerState.PAUSED, MediaPlayerState.COMPLETED -> {
                repository.startMediaPlayer()
                mediaPlayerState = MediaPlayerState.STARTED
            }
            MediaPlayerState.STARTED -> {
                repository.pauseMediaPlayer()
                mediaPlayerState = MediaPlayerState.PAUSED
            }
            else -> {}
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
        return mediaPlayerState == MediaPlayerState.STARTED
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        completionListener = listener
    }
}