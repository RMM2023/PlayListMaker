package com.practicum.playlistmaker.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.api.MediaPlayerInteractor
import com.practicum.playlistmaker.domain.interactor.FavoriteTracksInteractor
import com.practicum.playlistmaker.domain.interactor.PlaylistsInteractor
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class AudioPlayerViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val favoriteTracksInteractor: FavoriteTracksInteractor,
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    private val _track = MutableLiveData<Track>()
    val track: LiveData<Track> = _track

    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean> = _isPlaying

    private val _currentPosition = MutableLiveData<String>()
    val currentPosition: LiveData<String> = _currentPosition

    private var updatePositionJob: Job? = null

    private val _currentTrack = MutableLiveData<Track>()
    val currentTrack : LiveData<Track> = _currentTrack

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite : LiveData<Boolean> = _isFavorite

    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> = _playlists

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    init {
        _isPlaying.value = false
        _currentPosition.value = "00:00"
        refreshPlaylists()
    }


    fun refreshPlaylists() {
        viewModelScope.launch {
            playlistsInteractor.getPlaylists().collect { playlistsList ->
                _playlists.value = playlistsList
            }
        }
    }

    fun addTrackToPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            val track = _track.value ?: return@launch
            try {
                playlistsInteractor.addTrackToPlaylist(playlist, track)
                _message.value = "Добавлено в плейлист ${playlist.name}"
            } catch (e: Exception) {
                if (e is IllegalStateException && e.message == "Track already exists in playlist") {
                    _message.value = "Трек уже добавлен в плейлист ${playlist.name}"
                } else {
                    _message.value = "Ошибка при добавлении трека в плейлист"
                }
            }
        }
    }

    fun initTrack(track: Track) {
        _track.value = track
        track.previewUrl?.let { mediaPlayerInteractor.initMediaPlayer(it) }
        _isPlaying.value = false
        _currentPosition.value = "00:00"
        _currentTrack.value = track
        viewModelScope.launch {
            val favoriteTracksIds = favoriteTracksInteractor.getFavoriteTracksIds().first()
            _isFavorite.value = favoriteTracksIds.contains(track.trackId)
        }

        mediaPlayerInteractor.setOnCompletionListener {
            onPlaybackCompleted()
        }
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
        updatePositionJob?.cancel()
        updatePositionJob = viewModelScope.launch {
            while (isActive) {
                updatePosition()
                delay(300)
            }
        }
    }

    private fun stopUpdatingPosition() {
        updatePositionJob?.cancel()
    }

    private fun updatePosition() {
        val currentPosition = mediaPlayerInteractor.getCurrentPosition()
        _currentPosition.value = SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentPosition)
    }

    private fun onPlaybackCompleted() {
        _isPlaying.value = false
        _currentPosition.value = "00:00"
        stopUpdatingPosition()
    }

    fun formatTrackTime(timeMillis: Long): String {
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
    fun onFavoriteClicked(){
        val track = _currentTrack.value ?: return
        viewModelScope.launch {
            if (track.isFavorite){
                favoriteTracksInteractor.deleteTracK(track)
            }else{
                favoriteTracksInteractor.insertTrack(track)
            }
            track.isFavorite = !track.isFavorite
            _isFavorite.postValue(track.isFavorite)
        }
    }
}