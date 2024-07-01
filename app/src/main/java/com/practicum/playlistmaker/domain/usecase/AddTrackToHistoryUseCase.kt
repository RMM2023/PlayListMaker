package com.practicum.playlistmaker.domain.usecase

import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.repository.SearchHistoryRepository

class AddTrackToHistoryUseCase(private val repository: SearchHistoryRepository) {
    fun execute(track: Track) = repository.addTrackToHistory(track)
}