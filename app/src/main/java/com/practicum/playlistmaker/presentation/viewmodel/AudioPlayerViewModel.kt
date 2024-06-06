package com.practicum.playlistmaker.presentation.viewmodel

import android.widget.ImageView
import com.practicum.playlistmaker.domain.usecase.GetTrackUseCase
import com.practicum.playlistmaker.presentation.model.TrackUIModel
import com.practicum.playlistmaker.data.utils.MediaPlayerManager
import com.practicum.playlistmaker.data.utils.ImageLoader

class AudioPlayerViewModel(
    private val getTrackUseCase: GetTrackUseCase,
    private val mediaPlayerManager: MediaPlayerManager,
    private val imageLoader: ImageLoader
) {
    interface Callback<T> {
        fun onSuccess(data: T)
        fun onError(error: Throwable)
    }

    fun getTrack(json: String?, callback: Callback<TrackUIModel>) {
        try {
            val track = getTrackUseCase.execute(json)
            track?.let {
                callback.onSuccess(TrackUIModel.fromDomain(it))
            } ?: run {
                callback.onError(NullPointerException("Track not found"))
            }
        } catch (e: Exception) {
            callback.onError(e)
        }
    }

    fun prepareMediaPlayer(url: String, onPrepared: () -> Unit, onCompletion: () -> Unit) {
        mediaPlayerManager.prepareMediaPlayer(url, onPrepared, onCompletion)
    }

    fun playPause() {
        if (mediaPlayerManager.isPlaying()) {
            mediaPlayerManager.pause()
        } else {
            mediaPlayerManager.play()
        }
    }

    fun releaseMediaPlayer() {
        mediaPlayerManager.release()
    }

    fun isPlaying(): Boolean {
        return mediaPlayerManager.isPlaying()
    }

    fun getCurrentPosition(): Int {
        return mediaPlayerManager.getCurrentPosition()
    }

    fun loadImage(url: String, imageView: ImageView) {
        imageLoader.loadImage(url, imageView)
    }
}