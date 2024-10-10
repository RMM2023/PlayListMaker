package com.practicum.playlistmaker.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.usecase.SearchTracksUseCase
import com.practicum.playlistmaker.domain.usecase.GetSearchHistoryUseCase
import com.practicum.playlistmaker.domain.usecase.AddTrackToHistoryUseCase
import com.practicum.playlistmaker.domain.usecase.ClearSearchHistoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

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

    private val searchQueryFlow = MutableStateFlow("")

    init {
        loadSearchHistory()
        setupSearchQueryDebounce()
    }

    private fun setupSearchQueryDebounce() {
        searchQueryFlow
            .debounce(2000)
            .onEach { query ->
                if (query.isNotEmpty()) {
                    search(query)
                }
            }
            .launchIn(viewModelScope)
    }

    fun updateSearchQuery(query: String) {
        searchQueryFlow.value = query
        if (query.isEmpty()) {
            clearResults()
            loadSearchHistory()
        }
    }

    fun search(query: String) {
        _errorMessage.value = null
        clearResults()
        _isLoading.value = true
        viewModelScope.launch {
            try {
                // Получаем первый результат из потока
                val result = searchTracksUseCase.execute(query).first()
                _searchResults.value = result.results
            } catch (e: Exception) {
                _errorMessage.value = "Error occurred while searching"
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun addTrackToHistory(track: Track) {
        viewModelScope.launch {
            addTrackToHistoryUseCase.execute(track)
        }
    }

    fun clearSearchHistory() {
        viewModelScope.launch {
            clearSearchHistoryUseCase.execute()
            loadSearchHistory()
        }
    }

    fun loadSearchHistory() {
        _searchHistory.value = getSearchHistoryUseCase.execute()
    }

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    fun clearResults() {
        _searchResults.value = emptyList()
    }
}