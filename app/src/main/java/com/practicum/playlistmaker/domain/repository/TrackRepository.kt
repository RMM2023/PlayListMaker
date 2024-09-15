package com.practicum.playlistmaker.domain.repository


import com.practicum.playlistmaker.domain.model.SearchResult
import kotlinx.coroutines.flow.Flow


interface TrackRepository {
    fun searchTracks(term: String) : Flow<SearchResult>
}