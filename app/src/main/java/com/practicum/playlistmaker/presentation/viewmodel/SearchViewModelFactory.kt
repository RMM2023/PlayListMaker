package com.practicum.playlistmaker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.domain.usecase.SearchTracksUseCase
import com.practicum.playlistmaker.domain.usecase.GetSearchHistoryUseCase
import com.practicum.playlistmaker.domain.usecase.AddTrackToHistoryUseCase
import com.practicum.playlistmaker.domain.usecase.ClearSearchHistoryUseCase

class SearchViewModelFactory(
    private val searchTracksUseCase: SearchTracksUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val addTrackToHistoryUseCase: AddTrackToHistoryUseCase,
    private val clearSearchHistoryUseCase: ClearSearchHistoryUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(
                searchTracksUseCase,
                getSearchHistoryUseCase,
                addTrackToHistoryUseCase,
                clearSearchHistoryUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}