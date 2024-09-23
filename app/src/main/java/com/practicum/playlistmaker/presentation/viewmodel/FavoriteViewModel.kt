package com.practicum.playlistmaker.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.interactor.FavoriteTracksInteractor
import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.launch

class FavoriteViewModel(private val interactor: FavoriteTracksInteractor) : ViewModel() {
    private val _favoriteTracks = MutableLiveData<List<Track>>()
    val favoriteTracks : LiveData<List<Track>> = _favoriteTracks
    fun loadFavoriteTracks(){
        viewModelScope.launch {
            interactor.getFavoriteTracks().collect{tracks ->
                _favoriteTracks.postValue(tracks)
            }
        }
    }
    init {
        loadFavoriteTracks()
    }
}