package com.practicum.playlistmaker.domain.model

data class SearchResult(
    val resultCount: Int,
    val results: List<Track>
)