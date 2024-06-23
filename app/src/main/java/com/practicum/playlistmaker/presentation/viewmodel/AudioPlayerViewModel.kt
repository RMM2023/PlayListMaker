package com.practicum.playlistmaker.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.api.MediaPlayerInteractor
import com.practicum.playlistmaker.domain.model.Track
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class AudioPlayerViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor
) : ViewModel() {

    private val _track = MutableLiveData<Track>()
    val track: LiveData<Track> = _track

    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean> = _isPlaying

    private val _currentPosition = MutableLiveData<String>()
    val currentPosition: LiveData<String> = _currentPosition

    private var updatePositionJob: java.util.Timer? = null

    init {
        _isPlaying.value = false // Устанавливаем начальное значение false
        _currentPosition.value = "00:00" // Устанавливаем начальное значение времени
    }

    fun initTrack(track: Track) {
        _track.value = track
        mediaPlayerInteractor.initMediaPlayer(track.previewUrl)
        _isPlaying.value = false // Сбрасываем состояние воспроизведения при инициализации нового трека
        _currentPosition.value = "00:00" // Сбрасываем позицию воспроизведения
    }

    fun togglePlayPause() {
        mediaPlayerInteractor.playPauseMediaPlayer()
        _isPlaying.value = mediaPlayerInteractor.isPlaying()
        if (_isPlaying.value == true) {
            startUpdatingPosition()
        } else {
            stopUpdatingPosition()
        }
    }

    private fun startUpdatingPosition() {
        updatePositionJob = java.util.Timer()
        updatePositionJob?.schedule(object : java.util.TimerTask() {
            override fun run() {
                updatePosition()
            }
        }, 0, 500)
    }

    private fun stopUpdatingPosition() {
        updatePositionJob?.cancel()
        updatePositionJob = null
    }

    private fun updatePosition() {
        val currentPosition = mediaPlayerInteractor.getCurrentPosition()
        _currentPosition.postValue(SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentPosition))
    }

    fun formatTrackTime(timeMillis: Int): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(timeMillis)
    }

    fun formatYear(releaseDate: String): String {
        return LocalDateTime.parse(
            releaseDate,
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        ).year.toString()
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayerInteractor.releaseMediaPlayer()
        stopUpdatingPosition()
    }
}