package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.model.SearchResult

interface TrackRepository {
    fun searchTracks(term: String, callback: (result: Result<SearchResult>) -> Unit)
}