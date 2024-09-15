package com.practicum.playlistmaker.domain.usecase

import com.practicum.playlistmaker.domain.model.SearchResult
import com.practicum.playlistmaker.domain.repository.TrackRepository
import kotlinx.coroutines.flow.Flow

class SearchTracksUseCase(private val repository: TrackRepository) {
    fun execute(term: String) : Flow<SearchResult> {
        return repository.searchTracks(term)
    }
}