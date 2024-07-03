package com.practicum.playlistmaker.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.usecase.SearchTracksUseCase
import com.practicum.playlistmaker.domain.usecase.GetSearchHistoryUseCase
import com.practicum.playlistmaker.domain.usecase.AddTrackToHistoryUseCase
import com.practicum.playlistmaker.domain.usecase.ClearSearchHistoryUseCase

class SearchViewModel(
    private val searchTracksUseCase: SearchTracksUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val addTrackToHistoryUseCase: AddTrackToHistoryUseCase,
    private val clearSearchHistoryUseCase: ClearSearchHistoryUseCase
) : ViewModel() {

    private val _searchResults = MutableLiveData<List<Track>>()
    val searchResults: LiveData<List<Track>> = _searchResults

    private val _searchHistory = MutableLiveData<List<Track>>()
    val searchHistory: LiveData<List<Track>> = _searchHistory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        loadSearchHistory()
    }

    fun search(query: String) {
        _errorMessage.value = null
        clearResults()
        _isLoading.value = true
        searchTracksUseCase.execute(query) { result ->
            _isLoading.postValue(false)
            result.fold(
                onSuccess = { searchResult ->
                    _searchResults.postValue(searchResult.results)
                },
                onFailure = {
                    _errorMessage.postValue("Error occurred while searching")
                    _searchResults.postValue(emptyList())
                }
            )
        }
    }

    fun addTrackToHistory(track: Track) {
        addTrackToHistoryUseCase.execute(track)
        loadSearchHistory()
    }

    fun clearSearchHistory() {
        clearSearchHistoryUseCase.execute()
        loadSearchHistory()
    }

    fun loadSearchHistory() {
        _searchHistory.value = getSearchHistoryUseCase.execute()
    }

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    fun clearResults(){
        _searchResults.value = emptyList()
    }
}