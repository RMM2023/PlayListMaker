package com.practicum.playlistmaker.presentation.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.interactor.PlaylistsInteractor
import com.practicum.playlistmaker.domain.model.PlaylistState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewPlaylistViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel()  {
    private val _playlistsState = MutableStateFlow<PlaylistState>(PlaylistState.Empty)
    val playlistState: StateFlow<PlaylistState> = _playlistsState

    private val _savedCoverUri = MutableLiveData<Uri?>()
    val savedCoverUri: LiveData<Uri?> = _savedCoverUri

    fun newPlaylist(playlistName: String, playlistDescription: String, coverUri: Uri?) {
        viewModelScope.launch {
            playlistsInteractor.newPlaylist(playlistName, playlistDescription, coverUri)
        }
    }

    fun saveCoverToStorage(previewUri: Uri, context: Context) {
        viewModelScope.launch {
            val savedUri = playlistsInteractor.saveCoverToPrivateStorage(previewUri, context)
            _savedCoverUri.postValue(savedUri)
        }
    }
}