package com.practicum.playlistmaker.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.interactor.PlaylistsInteractor
import com.practicum.playlistmaker.domain.model.PlaylistState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayListViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    private val _playlistsState = MutableLiveData<PlaylistState>(PlaylistState.Load)
    val playlistState: MutableLiveData<PlaylistState> = _playlistsState

    private fun setState(playlistState: PlaylistState) {
        _playlistsState.postValue(playlistState)
    }


    fun getPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor
                .getPlaylists()
                .collect { playlists ->
                    val playlistsWithParsedUri = playlists.map { playlist ->
                        playlist.copy(
                            imageUri = Uri.parse(playlist.imageUri).toString()
                        )
                    }
                    if (playlistsWithParsedUri.isEmpty()) {
                        setState(PlaylistState.Empty)
                    } else {
                        setState(PlaylistState.Data(playlistsWithParsedUri))
                    }
                }
        }
    }
}