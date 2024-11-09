package com.practicum.playlistmaker.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.interactor.PlaylistsInteractor
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrentPlaylistViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    private lateinit var tracks: List<Track>

    private val _playlistId = MutableLiveData<Playlist>()
    fun observePlaylistId(): LiveData<Playlist> = _playlistId

    private val _playlistTracks = MutableLiveData<List<Track>>()
    fun observePlaylistTracks(): LiveData<List<Track>> = _playlistTracks

    private val _trackCount = MutableLiveData<Int>()
    fun observeTrackCount(): LiveData<Int> = _trackCount

    private val _playlistTime = MutableLiveData<Long>()
    fun observePlaylistAllTime(): LiveData<Long> = _playlistTime


    fun playlistAllTime() {
        if (_playlistTracks.value != null) {
            var time: Long = 0
            _playlistTracks.value?.forEach {
                time += it.trackTimeMillis ?: 0
            }
            _playlistTime.postValue(time)
        }
    }

    fun getPlaylistById(playlistId: Int) {
        viewModelScope.launch {
            _playlistId.postValue(playlistsInteractor.getPlaylistById(playlistId))
        }
    }

    fun getAllTracks(playlistId: List<Long>) {
        viewModelScope.launch(Dispatchers.IO) {
            tracks = playlistsInteractor.getAllTracks(playlistId)
            _playlistTracks.postValue(tracks)
        }
    }

    fun deleteTrackFromPlaylist(playlist: Playlist, trackId: Long) {
        val playlistId = _playlistId.value?.id
        viewModelScope.launch(Dispatchers.IO) {
            playlist.tracksAmount = playlist.tracksIds.size - 1
            if (playlistId != null) {
                playlistsInteractor.deleteTrackFromPlaylist(playlistId, trackId)
                trackCountDecrease(playlistId)
                updatePlaylist(playlistId)
            }
        }
    }

    fun deletePlaylist() {
        val playlistId = _playlistId.value?.id
        viewModelScope.launch(Dispatchers.IO) {
            if (playlistId != null) {
                playlistsInteractor.deletePlaylist(playlistId)
            }
        }
    }

    private fun updatePlaylist(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val update = playlistsInteractor.getPlaylistById(playlistId)
            _playlistId.postValue(update)
            val updateTracks = playlistsInteractor.getAllTracks(update.tracksIds)
            _playlistTracks.postValue(updateTracks)
            _trackCount.postValue(updateTracks.size)
        }
    }

    private suspend fun trackCountDecrease(playlistId: Int) {
        playlistsInteractor.trackCountDecrement(playlistId)
    }
}