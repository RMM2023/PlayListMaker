package com.practicum.playlistmaker.domain.usecase

import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.repository.SearchHistoryRepository

class GetSearchHistoryUseCase(private val repository: SearchHistoryRepository) {
    fun execute(): List<Track> = repository.getSearchHistory()
}