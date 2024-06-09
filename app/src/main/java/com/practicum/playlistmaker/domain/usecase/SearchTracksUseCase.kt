package com.practicum.playlistmaker.domain.usecase

import com.practicum.playlistmaker.domain.model.SearchResult
import com.practicum.playlistmaker.domain.repository.TrackRepository

class SearchTracksUseCase(private val repository: TrackRepository) {
    fun execute(term: String, callback: (result: Result<SearchResult>) -> Unit) {
        repository.searchTracks(term, callback)
    }
}